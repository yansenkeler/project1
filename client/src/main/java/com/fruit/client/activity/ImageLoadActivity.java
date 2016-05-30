package com.fruit.client.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import com.fruit.client.R;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cocosw.bottomsheet.BottomSheet;
import com.fruit.common.img.ImageUtil;
import com.fruit.common.service.ImageIntentManager;

/**
 * Created by user on 2016/2/29.
 */
public class ImageLoadActivity extends BaseActivity {
    private static final int REQUEST_ALBUM = 0;
    private Button mButton;

    @Override
    public int setContentLayout() {
        return R.layout.activity_image_load;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mButton = (Button)findViewById(R.id.button_image);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BottomSheet.Builder(ImageLoadActivity.this).title("选择照片").sheet(R.menu.menu_takephoto)
                        .listener(new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case R.id.album:
                                        ImageIntentManager.startAlbum(ImageLoadActivity.this, REQUEST_ALBUM);
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }).show();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode== RESULT_OK){
            switch (requestCode){
                case REQUEST_ALBUM:
                    Uri imgUri = null;
                    if(data!=null){
                        imgUri = data.getData();
                    }
                    if(imgUri!=null){
                        Bitmap mBitmap = ImageUtil.getBitmapFromUri(ImageLoadActivity.this, imgUri);
                    }
                    break;
                default:
                    break;
            }
        }
    }

}
