package com.zpk.console.web.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.util.EncodingUtil;

/**
 * 解决中文文件名乱码
 */
public class CustomFilePart extends FilePart {
    public CustomFilePart(String filename, File file)
            throws FileNotFoundException {
        super(filename, file);
    }

    public CustomFilePart(String filename, File file, String charset)
            throws FileNotFoundException {
        super(filename, file, null, charset);
    }

    protected void sendDispositionHeader(OutputStream out) throws IOException {
        // http://www.it610.com/article/487248.htm
        // 实现基类Part方法
        out.write(CONTENT_DISPOSITION_BYTES);
        out.write(QUOTE_BYTES);
        out.write(EncodingUtil.getBytes(getName(), "utf-8"));//OK，通过
        out.write(QUOTE_BYTES);
//        super.sendDispositionHeader(out);
// 实现父类FilePart方法
        String fileName = getSource().getFileName();
        if (fileName != null) {
            out.write(EncodingUtil.getAsciiBytes(FILE_NAME));
            out.write(QUOTE_BYTES);
            out.write(EncodingUtil.getBytes(fileName, "utf-8"));//OK，通过
            out.write(QUOTE_BYTES);
        }
    }
}
