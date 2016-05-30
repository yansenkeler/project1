package com.fruit.common.nfc.util;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.fruit.common.nfc.data.MifareBlock;
import com.fruit.common.nfc.data.MifareClassCard;
import com.fruit.common.nfc.data.MifareSector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by liangchen on 15/6/26.
 */
public class ActivityNfcUtil {

    /*###################写操作 start#########################*/
    public static boolean writeTag(Context context, Tag tag ,short sector,int block,byte[] key,String content) {

        MifareClassic mfc = MifareClassic.get(tag);

        try {
            mfc.connect();
            boolean auth = false;
            auth = mfc.authenticateSectorWithKeyA(sector,
                    key);
            if (auth) {
                // the last block of the sector is used for KeyA and KeyB cannot be overwritted
                int bIndex;
                bIndex = mfc.sectorToBlock(sector);
                mfc.writeBlock(bIndex+block, content.getBytes());
                mfc.close();
                Toast.makeText(context, "写入成功", Toast.LENGTH_SHORT).show();
                return true;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e("nfcerror",e.getMessage());
            Toast.makeText(context, "写入失败", Toast.LENGTH_SHORT).show();
        } finally {
            try {
                mfc.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Toast.makeText(context, "写入失败", Toast.LENGTH_SHORT).show();
            }
        }
        return false;
    }

    public static boolean writeByteToTag(Context context, Tag tag ,short sector,int block,byte[] key,byte[] content) {

        MifareClassic mfc = MifareClassic.get(tag);

        try {
            mfc.connect();
            boolean auth = false;
            auth = mfc.authenticateSectorWithKeyA(sector,
                    key);
            if (auth) {
                // the last block of the sector is used for KeyA and KeyB cannot be overwritted
                int bIndex;
                bIndex = mfc.sectorToBlock(sector);
                mfc.writeBlock(bIndex+block, content);
                mfc.close();
                Toast.makeText(context, "写入成功", Toast.LENGTH_SHORT).show();
                return true;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e("nfcerror",e.getMessage());
            Toast.makeText(context, "写入失败", Toast.LENGTH_SHORT).show();
        } finally {
            try {
                mfc.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Toast.makeText(context, "写入失败", Toast.LENGTH_SHORT).show();
            }
        }
        return false;
    }

    public static boolean writeKey(Context context, Tag tag ,short sector,int block,byte[] key,byte[] newKey) {

        MifareClassic mfc = MifareClassic.get(tag);

        try {
            mfc.connect();
            boolean auth = false;
            auth = mfc.authenticateSectorWithKeyA(sector,
                    key);
            if (auth) {
                // the last block of the sector is used for KeyA and KeyB cannot be overwritted
                int bIndex;
                bIndex = mfc.sectorToBlock(sector);
                mfc.writeBlock(bIndex+block, newKey);
                mfc.close();
                Toast.makeText(context, "写入成功", Toast.LENGTH_SHORT).show();
                return true;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e("nfcerror",e.getMessage());
            Toast.makeText(context, "写入失败", Toast.LENGTH_SHORT).show();
        } finally {
            try {
                mfc.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Toast.makeText(context, "写入失败", Toast.LENGTH_SHORT).show();
            }
        }
        return false;
    }
    /*###################写操作 end#########################*/

    /*###################读操作 start#########################*/
    public static byte[] readTag(Context context, Tag tag,short sector,int block,byte[] key) {

        MifareClassic mfc = MifareClassic.get(tag);
        for (String tech : tag.getTechList()) {
            System.out.println(tech);
        }
        boolean auth = false;
        //读取TAG

        try {
            //Enable I/O operations to the tag from this TagTechnology object.
            mfc.connect();
            int type = mfc.getType();//获取TAG的类型
            int sectorCount = mfc.getSectorCount();//获取TAG中包含的扇区数
            String typeS = "";
            switch (type) {
                case MifareClassic.TYPE_CLASSIC:
                    typeS = "TYPE_CLASSIC";
                    break;
                case MifareClassic.TYPE_PLUS:
                    typeS = "TYPE_PLUS";
                    break;
                case MifareClassic.TYPE_PRO:
                    typeS = "TYPE_PRO";
                    break;
                case MifareClassic.TYPE_UNKNOWN:
                    typeS = "TYPE_UNKNOWN";
                    break;
            }

            //Authenticate a sector with key A.
            auth = mfc.authenticateSectorWithKeyA(sector,
                    key);
            int bIndex;
            if (auth) {
                // 读取扇区中的块
                bIndex = mfc.sectorToBlock(sector);

                byte[] data = mfc.readBlock(bIndex+block);
                String str2 = new String(data);
//                    metaInfo += "Block " + bIndex + " : "
//                            + bytesToHexString(data) + "\n";
                return data;
            } else {
                Toast.makeText(context, "验证失败", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } finally {
            if (mfc != null) {
                try {
                    mfc.close();
                } catch (IOException e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG)
                            .show();
                }
            }
        }
        return null;

    }
    /*###################读操作 end#########################*/

    /*需要操作写入多个块的时候需实现该借口*/
    public interface BlockInterface{
        boolean dealBlock(MifareClassic mfc) throws Exception;
    }

    public static boolean dealMutiBlockToTag(Context context, Tag tag ,BlockInterface blockWriteInterface) {

        MifareClassic mfc = MifareClassic.get(tag);

        try {
            mfc.connect();
            return blockWriteInterface.dealBlock(mfc);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e("nfcerror",e.getMessage());
//            Toast.makeText(context, "卡操作失败", Toast.LENGTH_SHORT).show();
        } finally {
            try {
                mfc.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
//                Toast.makeText(context, "卡操作失败", Toast.LENGTH_SHORT).show();
            }
        }
        return false;
    }
}
