package org.daisy.pipeline.maven.xproc;

import java.io.File;

import com.xmlcalabash.util.URIUtils;
import org.apache.maven.plugin.MojoExecutionException;
import com.rackspace.cloud.api.docs.FileUtils;

import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

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
     *
     * @parameter
     *
     */
    private Map<String, String> inputs;

    /**
     * @parameter
     */
    private Map<String, String> parameters;


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

    /**
     * @parameter default-value="HE"
     */
    private String saxonEdition;


    protected static XProcEngine engine;

	
	public void execute() throws MojoExecutionException {

		FileUtils.extractJaredDirectory("olink",XProcMojo.class,new File(mavenBuildDir + "/docbkx/cloud"));
		
		System.setProperty("com.xmlcalabash.saxon-configuration", mavenBuildDir + "/docbkx/cloud/olink/saxon-configuration-" + saxonEdition + ".xml");
		ClassLoader cl = AbstractMojo.class.getClassLoader();
		try {
			Class.forName("com.xmlcalabash.drivers.Main", false, cl);
			engine = new Calabash(); }
			catch (ClassNotFoundException ee) {
				throw new RuntimeException("Could not find any XProc engines on the classpath."); }
		
	    Map<String, String> options=new HashMap<String, String>();
	    options.put("mavenBuildDir", utils.asURI(mavenBuildDir));
	    options.put("olinkManifest", utils.asURI(new File(mavenPomdir, olinkManifest)));
	    
		try {
		    System.out.println("pipeline: " + pipeline);

            Map<String, String> ins = new HashMap<String, String>();

            URI cwd = URIUtils.cwdAsURI();

            if( inputs != null && !inputs.isEmpty() ) {
                System.out.println( "inputs: " );
                for( String input : inputs.keySet() ) {

                    System.out.println( " - " + input + ": " + inputs.get( input ) );


                    ins.put( input, cwd.resolve( inputs.get( input ) ).toASCIIString() );
                }
            }

            Map<String, Map<String, String>> params = new HashMap<String, Map<String, String>>();

            if( parameters != null && !parameters.isEmpty() ) {

                for( String param : parameters.keySet() ) {

                    System.out.println( " - " + param + ": " );

                    Map<String, String> map = new HashMap<String, String>();
                    params.put( param, map );

                    StringTokenizer tokenizer = new StringTokenizer( parameters.get( param ) );

                    while( tokenizer.hasMoreTokens() ) {

                        String line = tokenizer.nextToken();

                        String[] pair = line.split( "=" );

                        if( pair.length != 2 ) {

                            throw new Exception( "The value of the '" + param
                                                       + "' '" + line + "' parameters value should be 2 strings separated by an '='." );
                        }

                        System.out.println( "   - " + line );

                        map.put( pair[ 0 ], pair[ 1 ] );

                    }
                }
            }

			engine.run(asURI(pipeline), ins, null, options, params );
			System.out.println("Running XProc ..."); }
		catch (Exception e) {
			throw new MojoExecutionException("Error running XProc", e);
        }
        finally {

            // clear system property to prevent any later saxon executions from getting these configurations
            System.clearProperty("com.xmlcalabash.saxon-configuration" );
        }
    }
}
