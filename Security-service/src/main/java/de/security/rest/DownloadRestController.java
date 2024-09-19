package de.security.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.security.service.BouncyCastleCertificateGenerator;
import de.security.service.CertificateService;
import de.security.utils.Certificate;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/download")
public class DownloadRestController {

	@RequestMapping(value = "/root", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> root() throws Exception {
		InputStreamResource resource = new InputStreamResource(org.apache.commons.io.IOUtils.toInputStream(Certificate.ROOT_CERTIFICATE, "UTF-8"));
		
	    HttpHeaders headers = new HttpHeaders();
	    headers.add("Content-Disposition", "attachment; filename=root.crt");
	    return ResponseEntity.ok().headers(headers).body(resource);
	}
	
	@RequestMapping(value = "/own", method = RequestMethod.GET)
	public void getFile(HttpServletResponse response) throws Exception {
		
		//CertificateService.listX509CertificatesFromStore("certs/cube-trust.p12", "password");
		
		RSAPublicKey isspub = CertificateService.loadRSAPublicKeyFromStore("certs/cube-trust.p12", "password", "cube issuing");
    	RSAPrivateKey isskey = CertificateService.loadRSAPrivateKeyFromStore("certs/cube-trust.p12", "password", "cube issuing pair");
    	X509Certificate isscert = CertificateService.loadX509CertificateFromStore("certs/cube-trust.p12", "password", "cube issuing");
    	X509Certificate intcert = CertificateService.loadX509CertificateFromStore("certs/cube-trust.p12", "password", "cube intermediate");
		
    	//RSAPublicKey isspub = CertificateService.loadRSAPublicKey(Certificate.ISS_PUBLIC);
    	//RSAPrivateKey isskey = CertificateService.loadRSAPrivateKey(Certificate.ISS_PRIVATE);
    	//X509Certificate isscert = CertificateService.loadX509Certificate(Certificate.ISS_CERTIFICATE);
    	//X509Certificate intcert = CertificateService.loadX509Certificate(Certificate.INT_CERTIFICATE);
    	
    	//CertificateService.writeX509CertificateToFile(isscert, "etc/cube-iss.crt");
    	//CertificateService.writeRSAPrivateKeyToFile(isskey, "etc/cube-iss.key");
    	//CertificateService.writeRSAPublicKeyToFile(isspub, "etc/cube-iss.pub");
    	
    	//CertificateService.writeX509CertificateToConsole(isscert);
    	//CertificateService.writeRSAPrivateKeyToConsole(isskey);
    	//CertificateService.writeRSAPublicKeyToConsole(isspub);
   	
    	BouncyCastleCertificateGenerator.createUserStore(intcert, isscert, isspub, isskey, "actionmanager", "etc/userCerts/");
		
    	File file = new File("etc/userCerts/actionmanager.p12");
	    response.setHeader("Content-Disposition", "attachment; filename=actionmanager.p12");

	    OutputStream out = response.getOutputStream();
	    FileInputStream in = new FileInputStream(file);

	    // copy from in to out
	    IOUtils.copy(in,out);

	    out.close();
	    in.close();
	    file.delete();
	}
}
