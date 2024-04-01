package cn.cuiot.dmp.base.application.dto;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import lombok.extern.slf4j.Slf4j;


/**
 * @author lixf
 */
@Slf4j
public class ResponseWrapper extends HttpServletResponseWrapper {

    private final WrapperServletOutputStream wrapperServletOutputStream = new WrapperServletOutputStream();

    public ResponseWrapper(HttpServletResponse response) {
        super(response);
    }


    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return wrapperServletOutputStream;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return new PrintWriter(wrapperServletOutputStream);
    }


    public byte[] getBodyBytes() {
        return wrapperServletOutputStream.out.toByteArray();
    }


    public String getBodyString() {
        try {
            return wrapperServletOutputStream.out.toString("UTF-8");
        } catch (UnsupportedEncodingException e) {
            return "[UNSUPPORTED ENCODING]";
        }
    }

    public void copyToResponse() {
        try {
            getResponse().getOutputStream().write(getBodyBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class WrapperServletOutputStream extends ServletOutputStream {
        private ByteArrayOutputStream out = new ByteArrayOutputStream();

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {
            log.info("setWriteListener");
        }

        @Override
        public void write(int b) {
            out.write(b);
        }


        @Override
        public void write(byte[] b) throws IOException {
            out.write(b);
        }


        @Override
        public void write(byte[] b, int off, int len) {
            out.write(b, off, len);
        }
    }
}