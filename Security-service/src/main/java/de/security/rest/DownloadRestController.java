package de.security.rest;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.nio.file.Files;
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

	/*@RequestMapping(value = "/own", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> own() throws Exception {
		RSAPublicKey isspub = CertificateService.readX509PublicKey(org.apache.commons.io.IOUtils.toInputStream(Certificate.ISS_PUBLIC, "UTF-8"));
    	RSAPrivateKey isskey = CertificateService.loadPrivateKey(org.apache.commons.io.IOUtils.toInputStream(Certificate.ISS_PRIVATE, "UTF-8"));
    	X509Certificate isscert = CertificateService.loadX509Certificate(org.apache.commons.io.IOUtils.toInputStream(Certificate.ISS_CERTIFICATE, "UTF-8"));
    	X509Certificate intcert = CertificateService.loadX509Certificate(org.apache.commons.io.IOUtils.toInputStream(Certificate.INT_CERTIFICATE, "UTF-8"));
    	
    	
    	BouncyCastleCertificateGenerator.createUserStore(intcert, isscert, isspub, isskey, "actionmanager", "etc/userCerts/");
		
		
		File file = new File("etc/userCerts/actionmanager.p12");
	    ByteArrayInputStream in = new ByteArrayInputStream(Files.readAllBytes(file.toPath()));	    
	    HttpHeaders headers = new HttpHeaders();
	    headers.add("Content-Disposition", "attachment; filename=actionmanager.p12");
	    return ResponseEntity.ok().headers(headers).body(new InputStreamResource(in));
	}*/
	
	@RequestMapping(value = "/root", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> root() throws Exception {
		InputStreamResource resource = new InputStreamResource(org.apache.commons.io.IOUtils.toInputStream(Certificate.ROOT_CERTIFICATE, "UTF-8"));
		
	    HttpHeaders headers = new HttpHeaders();
	    headers.add("Content-Disposition", "attachment; filename=root.crt");
	    return ResponseEntity.ok().headers(headers).body(resource);
	}
	
	@RequestMapping(value = "/own", method = RequestMethod.GET)
	public void getFile(HttpServletResponse response) throws Exception {
		RSAPublicKey isspub = CertificateService.readX509PublicKey(org.apache.commons.io.IOUtils.toInputStream(Certificate.ISS_PUBLIC, "UTF-8"));
    	RSAPrivateKey isskey = CertificateService.loadPrivateKey(org.apache.commons.io.IOUtils.toInputStream(Certificate.ISS_PRIVATE, "UTF-8"));
    	X509Certificate isscert = CertificateService.loadX509Certificate(org.apache.commons.io.IOUtils.toInputStream(Certificate.ISS_CERTIFICATE, "UTF-8"));
    	X509Certificate intcert = CertificateService.loadX509Certificate(org.apache.commons.io.IOUtils.toInputStream(Certificate.INT_CERTIFICATE, "UTF-8"));
    	
    	
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
