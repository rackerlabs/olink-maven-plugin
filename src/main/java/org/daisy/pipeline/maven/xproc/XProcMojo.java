package org.daisy.pipeline.maven.xproc;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;
import com.rackspace.cloud.api.docs.FileUtils;

import java.util.HashMap;
import java.util.Map;

import static org.daisy.pipeline.maven.xproc.utils.asURI;

/**
 * Run an XProc pipeline.
 *
 * @goal olink
 */
public class XProcMojo extends org.apache.maven.plugin.AbstractMojo {
	
	/**
	 * Path to the pipeline
	 *
	 * @parameter default-value="${project.build.directory}/docbkx/cloud/olink/olink.xpl"
	 * @required
	 */
	private File pipeline;

	/**
	 * Build dir.
	 *
	 * @parameter default-value="${project.build.directory}"
	 * @required
	 */
	private File mavenBuildDir;

        /**
	 * @parameter default-value="${project.basedir}"
         */
        private File mavenPomdir;

        /**
	 * @parameter default-value="olink.xml"
         */
        private String olinkManifest;
        
        
    	protected static XProcEngine engine;

	
	public void execute() throws MojoExecutionException {

		FileUtils.extractJaredDirectory("olink",XProcMojo.class,new File(mavenBuildDir + "/docbkx/cloud"));
		
		System.setProperty("com.xmlcalabash.saxon-configuration", mavenBuildDir + "/docbkx/cloud/olink/saxon-configuration.xml");
		ClassLoader cl = AbstractMojo.class.getClassLoader();
		try {
			Class.forName("com.xmlcalabash.drivers.Main", false, cl);
			engine = new Calabash(); }
		catch(ClassNotFoundException e) {
			try {
				Class.forName("org.daisy.pipeline.xproc.connect.XProcClient", false, cl);
				engine = new DaisyPipeline2(); }
			catch (ClassNotFoundException ee) {
				throw new RuntimeException("Could not find any XProc engines on the classpath."); }}
		
	    Map<String, String> options=new HashMap<String, String>();
	    options.put("mavenBuildDir", utils.asURI(mavenBuildDir));
	    options.put("olinkManifest", utils.asURI(new File(mavenPomdir, olinkManifest)));
	    
		try {
		    System.out.println("pipeline: " + pipeline); 
			engine.run(asURI(pipeline), null, null, options, null);
			System.out.println("Running XProc ..."); }
		catch (Exception e) {
			throw new MojoExecutionException("Error running XProc", e); }
	}
}
