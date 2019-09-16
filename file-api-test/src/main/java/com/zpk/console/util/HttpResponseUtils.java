package com.zpk.console.util;

import com.zpk.zcommon.enums.CharsetEnum;
import com.zpk.zcommon.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

public class HttpResponseUtils {

    /**
     * 下载文件，支持大文件、续传、速度限制。支持续传的响应头Accept-Ranges、ETag，请求头Range 。
     * Accept-Ranges：响应头，向客户端指明，此进程支持可恢复下载.实现后台智能传输服务（BITS），值为：bytes；
     * ETag：响应头，用于对客户端的初始（200）响应，以及来自客户端的恢复请求，
     * 必须为每个文件提供一个唯一的ETag值（可由文件名和文件最后被修改的日期组成），这使客户端软件能够验证它们已经下载的字节块是否仍然是最新的。
     * Range：续传的起始位置，即已经下载到客户端的字节数，值如：bytes=1474560- 。
     * 另外：UrlEncode编码后会把文件名中的空格转换中+（+转换为%2b），但是浏览器是不能理解加号为空格的，所以在浏览器下载得到的文件，空格就变成了加号；
     * 解决办法：UrlEncode 之后, 将 "+" 替换成 "%20"，因为浏览器将%20转换为空格
     *
     * @param fullPath 下载文件的物理路径，含路径、文件名
     * @return true下载成功，false下载失败
     */
    public static boolean writeFileBreakpoint(String fullPath, HttpServletRequest req, HttpServletResponse rep) throws Exception {
        return writeFileBreakpoint(fullPath, req, rep, 0, 0);
    }

    /**
     * 下载文件，支持大文件、续传、速度限制。支持续传的响应头Accept-Ranges、ETag，请求头Range 。
     * Accept-Ranges：响应头，向客户端指明，此进程支持可恢复下载.实现后台智能传输服务（BITS），值为：bytes；
     * ETag：响应头，用于对客户端的初始（200）响应，以及来自客户端的恢复请求，
     * 必须为每个文件提供一个唯一的ETag值（可由文件名和文件最后被修改的日期组成），这使客户端软件能够验证它们已经下载的字节块是否仍然是最新的。
     * Range：续传的起始位置，即已经下载到客户端的字节数，值如：bytes=1474560- 。
     * 另外：UrlEncode编码后会把文件名中的空格转换中+（+转换为%2b），但是浏览器是不能理解加号为空格的，所以在浏览器下载得到的文件，空格就变成了加号；
     * 解决办法：UrlEncode 之后, 将 "+" 替换成 "%20"，因为浏览器将%20转换为空格
     *
     * @param fullPath     下载文件的物理路径，含路径、文件名
     * @param bufferLength 分块读取缓存的大小
     * @param maxSpeed     下载最大的速度(KB)
     * @return true下载成功，false下载失败
     */
    public static boolean writeFileBreakpoint(String fullPath, HttpServletRequest req, HttpServletResponse rep,
                                              int bufferLength, int maxSpeed) throws Exception {
        bufferLength = bufferLength <= 0 ? 10240 : bufferLength;
        maxSpeed = maxSpeed <= 0 ? 2000 : maxSpeed;

        String method = req.getMethod().toUpperCase();
        //验证：HttpMethod，请求的文件是否存在
        switch (method) { //目前只支持GET和HEAD方法
            case "GET":
            case "HEAD":
                break;
            default:
                rep.setStatus(501);
                return false;
        }

        File file = new File(fullPath);
        if (!file.exists()) {
            rep.setStatus(404);
            return false;
        }

        String fileName = file.getName();
        //-- 验证：文件是否太大，是否是续传，且在上次被请求的日期之后是否被修
        long fileLen = file.length();
        if (fileLen > Integer.MAX_VALUE) {
            //-------文件太大了-------
            rep.setStatus(413);//请求实体太大
            return false;
        }


        String lastUpdateTiemStr = ZDateUtils.formatToRfc(ZFileUtils.getLastModifiedTimeUtc(file));
        //便于恢复下载时提取请求头;
        String eTag = ZStringUtils.urlEncode(fileName, CharsetEnum.UTF8.value()) + lastUpdateTiemStr;
        //对应响应头ETag：文件名+文件最后修改时间
        String ifRange = req.getHeader("If-Range");
        if (ZStringUtils.isNotEmpty(ifRange)) {
            //----------上次被请求的日期之后被修改过--------------
            if (ZObjectUtils.isNotEquals(ifRange.replace("\"", ""), eTag)) {
                //文件修改过
                rep.setStatus(412);//预处理失败
                return false;
            }
        }

        String encoding = CharsetEnum.UTF8.value();

        //------ - 添加重要响应头、解析请求头、相关验证------------------ -
//            httpContext.Response.Clear();
//            httpContext.Response.Buffer = false;
        //httpContext.Response.addHeader("Content-MD5", GetMD5Hash(myFile));//用于验证文件
        rep.setHeader("Accept-Ranges", "bytes");//重要：续传必须   //addHeader
        rep.setHeader("ETag", "\"" + eTag + "\"");//重要：续传必须
        rep.setHeader("Last-Modified", lastUpdateTiemStr);//把最后修改日期写入响应
        rep.setContentType("application/octet-stream");//MIME类型：匹配任意文件类型
        rep.setHeader("Content-Disposition", "attachment;filename=" + ZStringUtils.urlEncode(fileName, encoding));  //addHeader

//            rep.setHeader("Content-Length", fileLen + "");    //这样设置无效的
//            rep.setContentLengthLong(fileLen);//这样设置无效的

        rep.setHeader("Connection", "Keep-Alive");  //addHeader
        rep.setCharacterEncoding(encoding);
//            rep.ContentEncoding = Encoding.UTF8;

        long breakpoint = 0;
        //获取断点
        String hRange = req.getHeader("Range");
        if (ZStringUtils.isNotEmpty(hRange)) {
            //------如果是续传请求，则获取续传的起始位置，即已经下载到客户端的字节数------
            rep.setStatus(206); //重要：续传必须，表示局部范围响应。初始下载时默认为200

            //Range: bytes=(unit=first byte pos)(下载最后的位置就是breakpoint了)-[last byte pos]
            //Content-Range: bytes (unit first byte pos) - [last byte pos]/[entity legth]
            String[] ranges = ZStringUtils.split(hRange.replace("bytes", ""), "-", true);
            if (ZCollectionUtils.isNotEmpty(ranges)) {
                Integer ival = ZConvertUtils.tryParseInt(ZStringUtils.trim(ZStringUtils.trim(ranges[0], '=')));
                breakpoint = ZObjectUtils.getObject(ival, 0);
            }
            if (breakpoint < 0 || breakpoint >= fileLen) {
                //无效的起始位置
                return false;
            }
        }

//            if (breakpoint > 0)
        {
            //------如果是续传请求，告诉客户端本次的开始字节数，总长度，以便客户端将续传数据追加到breakpoint位置后-----
            rep.setHeader("Content-Range", getContentRangeVal(breakpoint, fileLen - 1, fileLen));   //addHeader
        }

        rep.flushBuffer();  //刷新一下


        //------ - 向客户端发送数据块------------------ -
        //speed为下载速度：每秒允许下载的字节数
        //int sleep = (int)Math.Ceiling(1000.0 * bufferLength / speed);//毫秒数：读取下一数据块的时间间隔

        int sleep = (int) Math.ceil(10000.0 * bufferLength / (maxSpeed * 1024));
        long blen = bufferLength > fileLen ? fileLen : bufferLength;

        byte[] datas = ZFileUtils.readFile(file.getPath(), breakpoint, blen);
        int readCount = datas.length;

        if (sleep > 1) {
            while (readCount > 0) {    //Response.IsClientConnected
                rep.getOutputStream().write(datas, 0, readCount);
                rep.flushBuffer();

                breakpoint += readCount;
                datas = ZFileUtils.readFile(file.getPath(), breakpoint, blen);
                readCount = datas.length;

                ZThreadUtils.sleep(sleep);
            }
        } else {
            while (readCount > 0) {    //Response.IsClientConnected
                rep.getOutputStream().write(datas, 0, readCount);
                rep.flushBuffer();

                breakpoint += readCount;
                datas = ZFileUtils.readFile(file.getPath(), breakpoint, blen);
                readCount = datas.length;

                //if (sleep > 1)
                //{
                //    System.Threading.Thread.Sleep(sleep);
                //}
            }
        }


//            rep.End();


        return true;
    }

    /**
     * 获取ContentRange的值
     */
    public static String getContentRangeVal(long startPoint, long endPoint, long fileLength) {
        return "bytes " + startPoint + "-" + endPoint + "/" + fileLength;
    }


}
