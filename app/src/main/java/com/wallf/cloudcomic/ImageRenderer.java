/*
 * Copyright (C) 2012 CyberAgent
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wallf.cloudcomic;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.LinkedList;
import java.util.Queue;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.OpenGlUtils;
import jp.co.cyberagent.android.gpuimage.Rotation;
import jp.co.cyberagent.android.gpuimage.util.TextureRotationUtil;

/**
 * 用于渲染拍照后保存图片
 *
 * @author singlee
 */
public class ImageRenderer implements Renderer {
    public static final int NO_IMAGE = -1;
    private static final float sVertices[] = {
            -1.0f, -1.0f,
            1.0f, -1.0f,
            -1.0f, 1.0f,
            1.0f, 1.0f,
    };
    private float[] mIndices = TextureRotationUtil.TEXTURE_NO_ROTATION;
    private GPUImageFilter mFilter;

    private int mGLTextureId = NO_IMAGE;
    private final FloatBuffer mVerticesBuffer;
    private final FloatBuffer mIndicesBuffer;
    // private IntBuffer mGLRgbBuffer;

    private int mOutputWidth;
    private int mOutputHeight;

    private final Queue<Runnable> mRunOnDraw;
    private final Queue<Runnable> mRunOnDrawEnd;
    private Rotation mRotation;
    private boolean mFlipHorizontal;
    private boolean mFlipVertical;

    public ImageRenderer(final GPUImageFilter filter) {
        mFilter = filter;
        mRunOnDraw = new LinkedList<Runnable>();
        mRunOnDrawEnd = new LinkedList<Runnable>();

        mVerticesBuffer = ByteBuffer.allocateDirect(sVertices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        mVerticesBuffer.put(sVertices).position(0);

        mIndicesBuffer = ByteBuffer
                .allocateDirect(mIndices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        mIndicesBuffer.put(mIndices).position(0);
    }

    @Override
    public void onSurfaceCreated(final GL10 unused, final EGLConfig config) {
        GLES20.glClearColor(0, 0, 0, 1);
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);
        mFilter.init();
    }

    @Override
    public void onSurfaceChanged(final GL10 gl, final int width, final int height) {
        mOutputWidth = width;
        mOutputHeight = height;
        mFilter.onOutputSizeChanged(width, height);
        GLES20.glViewport(0, 0, width, height);
        GLES20.glUseProgram(mFilter.getProgram());
        generateFrameBuffer(width, height);
    }

    @Override
    public void onDrawFrame(final GL10 gl) {
        GLES20.glClearColor(0, 0, 0, 1);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        runAll(mRunOnDraw);
        mFilter.onDraw(mGLTextureId, mVerticesBuffer, mIndicesBuffer);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        runAll(mRunOnDrawEnd);
    }

    private int[] mFrameBuffers = new int[1];
    private int mFrameBufferTexture = OpenGlUtils.NO_TEXTURE;

    /**
     * 生成FBO
     *
     * @param width
     * @param height
     */
    private void generateFrameBuffer(int width, int height) {

        int[] frameBufferTextures = new int[1];
        GLES20.glGenFramebuffers(1, mFrameBuffers, 0);
        GLES20.glGenTextures(1, frameBufferTextures, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, frameBufferTextures[0]);
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width,
                height, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);

        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
                GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
                GLES20.GL_CLAMP_TO_EDGE);

        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFrameBuffers[0]);
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER,
                GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D,
                frameBufferTextures[0], 0);

        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
        mFrameBufferTexture = frameBufferTextures[0];

        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFrameBuffers[0]);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
    }

    private void runAll(Queue<Runnable> queue) {
        synchronized (queue) {
            while (!queue.isEmpty()) {
                queue.poll().run();
            }
        }
    }

    public void setFilter(final GPUImageFilter filter) {
        runOnDraw(new Runnable() {

            @Override
            public void run() {
                final GPUImageFilter oldFilter = mFilter;
                mFilter = filter;
                if (oldFilter != null) {
                    oldFilter.destroy();
                }
                mFilter.init();
                mFilter.onOutputSizeChanged(mOutputWidth, mOutputHeight);
                GLES20.glUseProgram(mFilter.getProgram());
            }
        });
    }

    public void deleteImage() {
        runOnDraw(new Runnable() {

            @Override
            public void run() {
                GLES20.glDeleteTextures(1, new int[]{
                        mGLTextureId
                }, 0);
                mGLTextureId = NO_IMAGE;
            }
        });
    }

    public void setImageBitmap(final Bitmap bitmap) {
        setImageBitmap(bitmap, true);
    }

    // private int mAddedPadding;

    public void setImageBitmap(final Bitmap bitmap, final boolean recycle) {
        if (bitmap == null) {
            return;
        }

        runOnDraw(new Runnable() {


            @Override
            public void run() {
                // Bitmap resizedBitmap = null;
                // if (bitmap.getWidth() % 2 == 1) {
                // resizedBitmap = Bitmap.createBitmap(bitmap.getWidth() + 1,
                // bitmap.getHeight(), Bitmap.Config.ARGB_8888);
                // Canvas can = new Canvas(resizedBitmap);
                // can.drawARGB(0x00, 0x00, 0x00, 0x00);
                // can.drawBitmap(bitmap, 0, 0, null);
                // mAddedPadding = 1;
                // } else {
                // mAddedPadding = 0;
                // }
                mGLTextureId = OpenGlUtils.loadTexture(bitmap,
                        mGLTextureId, recycle);
                // activeFrameBuffer(mOutputWidth, mOutputHeight);
                // if (resizedBitmap != null) {
                // resizedBitmap.recycle();
                // }
            }
        });
    }

    public void setRotation(final Rotation rotation) {
        mRotation = rotation;

        float[] textureCords = TextureRotationUtil.getRotation(mRotation,
                mFlipHorizontal, mFlipVertical);
        mIndicesBuffer.clear();
        mIndicesBuffer.put(textureCords).position(0);
    }

    public void setRotation(final Rotation rotation,
                            final boolean flipHorizontal, final boolean flipVertical) {
        mFlipHorizontal = flipHorizontal;
        mFlipVertical = flipVertical;
        setRotation(rotation);
    }

    public Rotation getRotation() {
        return mRotation;
    }

    public boolean isFlippedHorizontally() {
        return mFlipHorizontal;
    }

    public boolean isFlippedVertically() {
        return mFlipVertical;
    }

    protected void runOnDraw(final Runnable runnable) {
        synchronized (mRunOnDraw) {
            mRunOnDraw.add(runnable);
        }
    }

    protected void runOnDrawEnd(final Runnable runnable) {
        synchronized (mRunOnDrawEnd) {
            mRunOnDrawEnd.add(runnable);
        }
    }
}
