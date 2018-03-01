package com.xlong.tupin.Utils;


import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.OSSObject;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class OSSCilentUtils {
    // endpoint以杭州为例，其它region请按实际情况填写
    private static final String endpoint = "http://oss-cn-shenzhen.aliyuncs.com";
    // 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维
    private static final String accessKeyId = "";
    private static final String accessKeySecret = "";
    // bucket name
    private static final String bucketName = "tupin";
    // 创建OSSClient实例
    private static final OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
    // 访问url的host
    private static final String host = "http://"+bucketName+".oss-cn-shenzhen.aliyuncs.com/";

    public static String OSSUpload(InputStream inputStream, String filedir){
        // 上传文件流
        ossClient.putObject(bucketName, filedir, inputStream);

        // 上传文件的url
        String url = host + filedir;

        return url;
    }

    public static void OSSDelete(String folder, String key){
        ossClient.deleteObject(bucketName, folder + key);
    }

    public static String createFolder(String folder){
        //文件夹名
        final String keySuffixWithSlash =folder;
        //判断文件夹是否存在，不存在则创建
        if(!ossClient.doesObjectExist(bucketName, keySuffixWithSlash)){
            //创建文件夹
            ossClient.putObject(bucketName, keySuffixWithSlash, new ByteArrayInputStream(new byte[0]));
            // 设置Object权限为公共读
            ossClient.setObjectAcl(bucketName, folder, CannedAccessControlList.PublicRead);

            //得到文件夹名
            OSSObject object = ossClient.getObject(bucketName, keySuffixWithSlash);
            String fileDir=object.getKey();
            return fileDir;
        }
        return keySuffixWithSlash;
    }

    public void destory(){
        ossClient.shutdown();
    }

}
