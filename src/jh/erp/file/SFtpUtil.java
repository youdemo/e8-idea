package jh.erp.file;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import weaver.general.BaseBean;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
public class SFtpUtil {

    /**
     * 密码方式登录 读取指定目录文件
     *
     * @param ip
     * @param user
     * @param psw
     * @param port
     */
    public  Session sshSftpReadFile(String ip, String user, String psw, int port) {
        Session session = null;
        JSch jsch = new JSch();
        try {
            if (port <= 0) {
                // 连接服务器，采用默认端口
                session = jsch.getSession(user, ip);
            } else {
                // 采用指定的端口连接服务器
                session = jsch.getSession(user, ip, port);
            }
            // 如果服务器连接不上，则抛出异常
            if (session == null) {
                throw new Exception("session is null");
            }
            // 设置登陆主机的密码
            session.setPassword(psw);// 设置密码
            // 设置第一次登陆的时候提示，可选值：(ask | yes | no)
            session.setConfig("StrictHostKeyChecking", "no");
            // 设置登陆超时时间
            session.connect(300000);
        } catch (Exception e) {
            new BaseBean().writeLog("session is null",e);
            e.printStackTrace();
        }
        return session;
    }

//    public  InputStream readFile(String sPath,String filename) {
//
//        Channel channel = null;
//        try {
//            channel = (Channel) session.openChannel("sftp");
//            channel.connect(10000000);
//            ChannelSftp sftp = (ChannelSftp) channel;
//            try {
//                sftp.cd(sPath);
//
//            } catch (SftpException e) {
//
//                sftp.mkdir(sPath);
//                sftp.cd(sPath);
//
//            }
//            Vector<ChannelSftp.LsEntry>  listFiles = sftp.ls(sftp.pwd());
//            for (ChannelSftp.LsEntry file : listFiles)
//            {
//                String fileName = file.getFilename();
//                try
//                {
//                    InputStream    inputStream = sftp.get(sftp.pwd() + "/" + fileName);
//
//                    stringHashMap.put(fileName,inputStream);
//                }
//                catch (SftpException e)
//                {
//                    e.printStackTrace();
//                }
//
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return stringHashMap;
//    }
}
