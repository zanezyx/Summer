package com.deter.TaxManager.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.deter.TaxManager.APPConstans;
import com.deter.TaxManager.model.SSID;
import com.deter.TaxManager.model.TopAP;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;

/**
 * 流转换工具
 */
public class StreamUtils<T> {

    /**
     * @param inputStream inputStream
     * @return 字符串转换之后的
     */
    public static String streamToString(InputStream inputStream) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                out.write(buffer, 0, len);
                out.flush();
            }

            String result = out.toString();
            out.close();
            inputStream.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 将对象保存到本地
     * @param context
     * @param fileName
     * @param bean
     * @return
     */
    public boolean writeObjectIntoLocal(Context context, String fileName, T bean) {
        try {
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_APPEND);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(bean);
            fos.close();
            oos.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 将集合保存到本地
     * @param fileName
     * @param list
     * @return T list
     */
    public boolean writeListIntoLocal(Context context, String fileName, List<T> list) {

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File sdCardDir = Environment.getExternalStorageDirectory();
            try {
                //FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_APPEND);
                FileOutputStream fos = new FileOutputStream(fileName, true);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(list);
                fos.close();
                oos.close();
                return true;
            } catch (FileNotFoundException e) {
                // TODO
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                // TODO
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }


    /**
     * 将对象写入sd卡
     *
     * @param fileName
     * @param bean
     * @return
     */
    public boolean writObjectIntoSDcard(String fileName, T bean) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File sdCardDir = Environment.getExternalStorageDirectory();
            File sdFile = new File(sdCardDir, fileName);
            try {
                FileOutputStream fos = new FileOutputStream(sdFile);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(bean);//写入
                fos.close();
                oos.close();
                return true;
            } catch (FileNotFoundException e) {
                // TODO
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                // TODO
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }


    /**
     * 将集合写入sd卡
     *
     * @param fileName
     * @param list
     * @return
     */
    public boolean writeListIntoSDcard(String fileName, List<T> list) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File sdCardDir = Environment.getExternalStorageDirectory();
            File sdFile = new File(sdCardDir, fileName);
            try {
                FileOutputStream fos = new FileOutputStream(sdFile);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(list);
                fos.close();
                oos.close();
                return true;
            } catch (FileNotFoundException e) {
                // TODO
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                // TODO
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }


    /**
     * 读取本地对象
     *
     * @param context
     * @param fielName
     * @return
     */
    public T readObjectFromLocal(Context context, String fielName) {
        T bean;
        try {
            FileInputStream fis = context.openFileInput(fielName);//获得输入流
            ObjectInputStream ois = new ObjectInputStream(fis);
            bean = (T) ois.readObject();
            fis.close();
            ois.close();
            return bean;
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
            return null;
        } catch (OptionalDataException e) {
            e.printStackTrace();
            return null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 读取本地集合
     * @param context
     * @param fielName
     * @return
     */
    public List<T> readListFromLocal(Context context, String fielName) {
        List<T> list;
        try {
            FileInputStream fis = context.openFileInput(fielName);//获得输入流
            ObjectInputStream ois = new ObjectInputStream(fis);
            list = (List<T>) ois.readObject();
            fis.close();
            ois.close();
            return list;
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
            return null;
        } catch (OptionalDataException e) {
            e.printStackTrace();
            return null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 读取sd卡对象
     *
     * @param fileName 文件名
     * @return
     */
    public T readObjectFromSdCard(String fileName) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            T bean;
            File sdCardDir = Environment.getExternalStorageDirectory();
            File sdFile = new File(sdCardDir, fileName);
            try {
                FileInputStream fis = new FileInputStream(sdFile);
                ObjectInputStream ois = new ObjectInputStream(fis);
                bean = (T) ois.readObject();
                fis.close();
                ois.close();
                return bean;
            } catch (StreamCorruptedException e) {
                // TODO
                e.printStackTrace();
                return null;
            } catch (OptionalDataException e) {
                // TODO
                e.printStackTrace();
                return null;
            } catch (FileNotFoundException e) {
                // TODO
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                // TODO
                e.printStackTrace();
                return null;
            } catch (ClassNotFoundException e) {
                // TODO
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 读取sd卡集合
     *
     * @param fileName 文件名
     * @return
     */
    public List<T> readListFromSdCard(String fileName) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            List<T> list;
            File sdCardDir = Environment.getExternalStorageDirectory();
            File sdFile = new File(sdCardDir, fileName);
            try {
                FileInputStream fis = new FileInputStream(sdFile);
                ObjectInputStream ois = new ObjectInputStream(fis);
                list = (List<T>) ois.readObject();
                fis.close();
                ois.close();
                return list;
            } catch (StreamCorruptedException e) {
                // TODO
                e.printStackTrace();
                return null;
            } catch (OptionalDataException e) {
                // TODO
                e.printStackTrace();
                return null;
            } catch (FileNotFoundException e) {
                // TODO
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                // TODO
                e.printStackTrace();
                return null;
            } catch (ClassNotFoundException e) {
                // TODO
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }



    public boolean writObjectIntoSDcard(String path, String fileName, String content) {
        FileWriter fw = null;
        BufferedWriter bw = null;

        try {
            File fileDir = new File(path);
            if (!fileDir.exists()) {
                Log.i("StreamUtils", "writObjectIntoSDcard >>>> current folder does not exist and is being created");
                if (fileDir.mkdirs()) {
                    Log.i("StreamUtils", " writObjectIntoSDcard >>>> current folder creates success");
                } else {
                    Log.i("StreamUtils", "writObjectIntoSDcard >>>> current folder creates fail Opration stop");
                    return false;
                }
            }

            File file = new File(path, fileName);
            if (!file.exists()) {
                Log.i("StreamUtils", "writObjectIntoSDcard >>>> current file does not exist and is being created");
                if (file.createNewFile()) {
                    Log.i("StreamUtils", "writObjectIntoSDcard >>>> current file creates success");
                } else {
                    Log.i("StreamUtils", "writObjectIntoSDcard >>>> current file creates fail Opration stop");
                    return false;
                }
            }

//            SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd" + " " + "hh:mm:ss");
//            String datetime = tempDate.format(new java.util.Date()).toString();
//            String myreadline = "[" + datetime + "] " + str;

            fw = new FileWriter(file, true);
            bw = new BufferedWriter(fw);
            bw.write(content + "\n");

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (null != bw) {
                    bw.newLine();
                    bw.flush();
                    bw.close();
                }
                if (null != fw) {
                    fw.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }



    public List<TopAP> readListFromSdCard(String path, String fileName){

        FileReader fr = null;
        BufferedReader br = null;
        List<TopAP> list = new ArrayList<>();

        try {
            File file1 = new File(path);
            // File file1 = new File(path, fileName);
            if (!file1.exists()) {
                Log.i("StreamUtils", "readListFromSdCard >>>> current file not exists");
                return list;
            }
            fr = new FileReader(file1);
            br = new BufferedReader(fr);

            String readStr;
            while ((readStr = br.readLine()) != null) {
                if (!TextUtils.isEmpty(readStr)) {
                    JSONObject jsonObject1 = new JSONObject(readStr);
                    JSONObject jsonObject2 = jsonObject1.getJSONObject("ssid");
                    String ssid = jsonObject2.getString("ssid");
                    String pwd = jsonObject2.getString("password");
                    String type = jsonObject2.getString("type");
                    //Log.e("result", "ssid=" + ssid + "    pwd=" + pwd + "   type=" + type);
                    TopAP topAP = new TopAP();
                    topAP.setSsid(ssid);
                    topAP.setPassword(pwd);
                    topAP.setType(type);
                    topAP.setEncryption(type);
                    list.add(topAP);
                }
            }

            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return list;
        } finally {
            try {
                if (null != fr) {
                    fr.close();
                }
                if (null != br) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();

            }
        }
    }


    /**
     * 从本地系统密码配置文件解析数据
     * @param path
     * @param fileName
     * @return
     */
    public List<SSID> readWifiConfigFromSdCard(String path, String fileName) {

        boolean isObject = false;
        SSID ssidEntity = null;
        //StringBuilder stringBuilder=null;

        FileReader fr = null;
        BufferedReader br = null;
        List<SSID> list = new ArrayList<>();
        try {
            File file1 = new File(path, fileName);
            if (!file1.exists()) {
                Log.i("StreamUtils", "readWifiConfigFromSdCard >>>> current file not exists");
                return list;
            }

            fr = new FileReader(file1);
            br = new BufferedReader(fr);

            String readStr;
            while ((readStr = br.readLine()) != null) {
                if (!TextUtils.isEmpty(readStr.toString())) {

                    if (!isObject) {
                        if (readStr.contains("network={")) {
                            isObject = true;
                            ssidEntity = new SSID();
                            //stringBuilder = new StringBuilder();
                        }
                    } else {

                        if (readStr.contains("ssid") && !readStr.contains("scan_ssid")) {
                            int qeIndex = readStr.indexOf("=");
                            String key = readStr.substring(0, qeIndex);
                            key = key.replace("\t", "");
                            if (key.equals("ssid")) {
                                int firstIndex = readStr.indexOf("\"");
                                //StringUtils.substringBefore(“dskeabcee”, “e”);
                                if (-1 != firstIndex) {
                                    int index = firstIndex + 1;
                                    int lastIndex = readStr.lastIndexOf("\"");
                                    readStr = readStr.substring(index, lastIndex);
                                    ssidEntity.setSsid(readStr);
                                    //stringBuilder.append("ssid=" + readStr);
                                } else {
                                    int index = readStr.indexOf("=");
                                    readStr = readStr.substring(index + 1);
                                    readStr = StringUtils.hexStringToString(readStr);
                                    ssidEntity.setSsid(readStr);
                                }
                            }
                        }

                        if (readStr.contains("psk")) {
                            int index = readStr.indexOf("\"") + 1;
                            int lastIndex = readStr.lastIndexOf("\"");
                            readStr = readStr.substring(index, lastIndex);
                            ssidEntity.setPassword(readStr);
                            //stringBuilder.append("  pwd=" + readStr);
                        }

                        if (readStr.contains("key_mgmt")) {
                            int firstIndex = readStr.indexOf("=");
                            readStr = readStr.substring(firstIndex + 1);
                            if (readStr.equalsIgnoreCase("NONE")) {
                                readStr = "OPEN";
                            }
                            ssidEntity.setType(readStr);
                            ssidEntity.setEncryption(readStr);
                            //stringBuilder.append("  type=" + readStr);
                        }

                        if (readStr.contains("}")) {
                            isObject = false;
                            list.add(ssidEntity);
                            //Log.e("result", stringBuilder.toString());
                        }
                    }

                }
            }

            return list;
        } catch (Exception e) {
           // Log.i("StreamUtils", "readWifiConfigFromSdCard >>>> " + android.util.Log.getStackTraceString(e.getCause()));
            e.printStackTrace();
            return list;
        } finally {
            try {
                if (null != fr) {
                    fr.close();
                }
                if (null != br) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 复制系统密码配置文件到SD卡目录
     * @param copyPath
     * @return
     */
    public boolean copyWifiConfigFromSdCard(String copyPath) {
        OutputStreamWriter osw;
        Process process;
        try {
            File localConfigFile = new File(copyPath, APPConstans.WIFI_CONFIG_FILE_NAME);
            if (localConfigFile.exists()) {
                localConfigFile.delete();
            }

            process = Runtime.getRuntime().exec("su");
            printMessage(process.getInputStream());
            printMessage(process.getErrorStream());
            File file = new File(copyPath);
            if (!file.exists()) {
                file.mkdirs();
            }
            String command = "cp /data/misc/wifi/wpa_supplicant.conf " + copyPath;
            osw = new OutputStreamWriter(process.getOutputStream());
            osw.write(command);
            osw.flush();
            osw.close();
            int result = process.waitFor();
            process.destroy();
            if (result == 0) {
                return true;
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return  false;
        }
    }

    private static void printMessage(final InputStream input) {
        new Thread(new Runnable() {
            public void run() {
                Reader reader = new InputStreamReader(input);
                BufferedReader bf = new BufferedReader(reader);
                String line = null;
                try {
                    while ((line = bf.readLine()) != null) {
                        System.out.println(line);
                        Log.d("xiaolu","line ="+line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }



}
