package org.softauto.jaxrs.auto;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.softauto.core.Configuration;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * create jaxrs client with ssl Authentication
 */
public class Ssl {



    public javax.ws.rs.client.Client getClient() throws Exception {
        return createMultiPartSecureClient(true, Configuration.get("jaxrs/certificate").asText());
    }

    public javax.ws.rs.client.Client createMultiPartSecureClient(boolean isMultiPartClient,String certificatePath) throws Exception {
        SSLContext sslContext = null;
        try {
            InputStream is = new FileInputStream(certificatePath);
            // You could get a resource as a stream instead.

            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate caCert = (X509Certificate)cf.generateCertificate(is);

            TrustManagerFactory tmf = TrustManagerFactory
                    .getInstance(TrustManagerFactory.getDefaultAlgorithm());
            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
            ks.load(null); // You don't need the KeyStore instance to come from a file.
            ks.setCertificateEntry("caCert", caCert);

            tmf.init(ks);

            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);


        }catch(Exception e){
            e.printStackTrace();
        }
        if(isMultiPartClient)
            return javax.ws.rs.client.ClientBuilder.newBuilder().sslContext(sslContext).register(MultiPartFeature.class).build();

        return javax.ws.rs.client.ClientBuilder.newBuilder().sslContext(sslContext).build();
    }


}
