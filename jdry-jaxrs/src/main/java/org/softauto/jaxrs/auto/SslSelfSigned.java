package org.softauto.jaxrs.auto;

import javax.net.ssl.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * create jaxrs client with ssl Authentication Self Signed
 */
public class SslSelfSigned {


    public javax.ws.rs.client.Client getClient() throws Exception {
        return createSelfSignedSecureClient();
    }

    public javax.ws.rs.client.Client createSelfSignedSecureClient() throws Exception {
        SSLContext sslcontext = null;
        HostnameVerifier allowAll = null;
        try {
            sslcontext = SSLContext.getInstance("TLS");
            sslcontext.init(null, new TrustManager[]{new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

            }}, new java.security.SecureRandom());

            allowAll = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
        }catch(Exception e){
            System.out.print(e);
        }
        return javax.ws.rs.client.ClientBuilder.newBuilder().sslContext(sslcontext).hostnameVerifier(allowAll).build();
    }
}
