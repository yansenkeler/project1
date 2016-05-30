package com.fruit.common.service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * Intent开启设备工具类
 * Created by user on 2016/2/29.
 */
public class ImageIntentManager {

    /**
     * 开启相册
     * @param mActivity
     * @param requestId
     */
    public static void startAlbum(Activity mActivity, int requestId) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");//相片类型
        mActivity.startActivityForResult(intent, requestId);
    }

    /**
     * 开启相机
     * @param mActivity
     * @param uri
     * @param requestId
     */
    public static void startCamera(Activity mActivity, Uri uri, int requestId) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        mActivity.startActivityForResult(intent, requestId);
    }

    /**
     * 开启截图
     * @param mActivity
     * @param inputUri
     * @param outputUri
     * @param requestId
     * @param outputX 输出图片x轴大小
     * @param outputY 输出图片y轴大小
     */
    public static void startCrop(Activity mActivity, Uri inputUri, Uri outputUri, int requestId, int outputX, int outputY) {
        Intent intent = new Intent();
        intent.setAction("keler.android.camera.action.CROP");
        intent.setDataAndType(inputUri, "image/*");// mUri是已经选择的图片Uri
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);// 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", outputX);// 输出图片大小
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        mActivity.startActivityForResult(intent, requestId);
    }
}
