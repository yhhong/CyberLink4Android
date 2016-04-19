package com.aspirecn.upnp.stb;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;

import com.aspirecn.upnp.stb.device.LightDevice;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by yinghuihong on 16/4/18.
 */
public class MainActivity extends AppCompatActivity implements LightDevice.RepaintListener {

    @Bind(R.id.cb_light)
    CheckBox cbLight;

    private LightDevice mLightDevice;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        CopyAssets("description", getCacheDir() + "/description/");

        try {
            mLightDevice = new LightDevice(getCacheDir() + "/description/description.xml");
            mLightDevice.setRepaintListener(this);

        } catch (Exception e) {
            e.printStackTrace();
        }
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                mLightDevice.start();
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    @Override
    protected void onDestroy() {
        mLightDevice.stop();
        super.onDestroy();
    }

    @Override
    public void repaint() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                cbLight.setChecked(mLightDevice.isOn());
            }
        });
    }


    private void CopyAssets(String assetDir, String dir) {
        String[] files;
        try {
            // 获得Assets一共有几多文件
            files = this.getResources().getAssets().list(assetDir);
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

        for (int i = 0; i < files.length; i++) {
            try {
                // 获得每个文件的名字
                String fileName = files[i];
                // 根据路径判断是文件夹还是文件
                if (!fileName.contains(".")) {
                    if (0 == assetDir.length()) {
                        CopyAssets(fileName, dir + fileName + "/");
                    } else {
                        CopyAssets(assetDir + "/" + fileName, dir + "/"
                                + fileName + "/");
                    }
                    continue;
                }
                File outFile = new File(mWorkingPath, fileName);
                if (outFile.exists())
                    outFile.delete();
                InputStream in = null;
                if (0 != assetDir.length())
                    in = getAssets().open(assetDir + "/" + fileName);
                else
                    in = getAssets().open(fileName);
                OutputStream out = new FileOutputStream(outFile);

                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }

                in.close();
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
