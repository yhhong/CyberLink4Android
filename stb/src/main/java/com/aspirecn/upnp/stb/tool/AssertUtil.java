package com.aspirecn.upnp.stb.tool;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Assert util
 * Created by yinghuihong on 16/4/20.
 */
public class AssertUtil {

    /**
     * copy asserts's files and folders to other location
     *
     * @param context
     * @param assetDir e.g. ""
     * @param dir      e.g. /data/packageName/cache/
     */
    public static void copyAssets(Context context, String assetDir, String dir) {
        String[] files;
        try {
            // 获得Assets一共有几多文件
            files = context.getResources().getAssets().list(assetDir);
        } catch (IOException e1) {
            return;
        }
        File mWorkingPath = new File(dir);
        // 如果文件路径不存在
        if (!mWorkingPath.exists()) {
            // 创建文件夹
            if (!mWorkingPath.mkdirs()) {
                // 文件夹创建不成功时调用
            }
        }
        for (String file : files) {
            try {
                // 获得每个文件的名字
                // 根据路径判断是文件夹还是文件
                if (!file.contains(".")) {
                    if (0 == assetDir.length()) {
                        copyAssets(context, file, dir + file + "/");
                    } else {
                        copyAssets(context, assetDir + "/" + file, dir + "/" + file + "/");
                    }
                    continue;
                }
                File outFile = new File(mWorkingPath, file);
                if (outFile.exists())
                    outFile.delete();
                InputStream in;
                if (0 != assetDir.length())
                    in = context.getAssets().open(assetDir + "/" + file);
                else
                    in = context.getAssets().open(file);
                OutputStream out = new FileOutputStream(outFile);

                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
