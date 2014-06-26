olink-maven-plugin
==================

This Maven plugin generates an olink target database file to be used when processing DocBook documents with DocBook xslts. For detailed information about the olink mechanism in the DocBook xslt stylesheets, see [Olinking between documents](http://www.sagehill.net/docbookxsl/Olinking.html) in Bob Stayton's book, _DocBook XSL: The Complete Guide_. 

This plugin takes as input a file named olink.xml in the same directory as your project's pom.xml file that lists the files to include in the olink data file. The olink.xml file should be in the following format. The path attribute on each book element is the relative path to the book to include in the olink database file. The target data file is generated as target/olink.db:

     <books xmlns="http://docs.rackspace.com/olink">
            <book path="src/docbkx/cf-intro.xml"/>
            <book path="src/docbkx/cf-devguide.xml"/>
            <book path="src/docbkx/cf-releasenotes.xml"/>
     </books>

In your project's pom, include the following in the <plugins> section of your pom.xml (where 1.2.6 is the latest release version of the olink-maven-plugin):

      <plugin>
        <groupId>com.rackspace.cloud.api</groupId>
        <artifactId>olink-maven-plugin</artifactId>
        <version>1.2.6</version>
        <executions>
          <execution>
            <phase>initialize</phase>
            <goals>
              <goal>olink</goal>
            </goals>
          </execution>
        </executions>
      </plugin>

In addition you should include the following profile as a direct child of the <project> element:

    <profiles>
        <profile>
            <id>Rackspace Research Repositories</id>
            <activation>
                <activeByDefault>true</activeByDefault>
            </activation>
            <repositories>
                <repository>
                    <id>rackspace-research</id>
                    <name>Rackspace Research Repository</name>
                    <url>http://maven.research.rackspacecloud.com/content/groups/public/</url>
                </repository>
            </repositories>
            <pluginRepositories>
                <pluginRepository>
                    <id>rackspace-research</id>
                    <name>Rackspace Research Repository</name>
                    <url>http://maven.research.rackspacecloud.com/content/groups/public/</url>
                </pluginRepository>
            </pluginRepositories>
        </profile>
    </profiles>


Limitations
===========

This plugin currently does not support controlling the relative path to other documents in the collection, but I do plan to add support for that in the future.

License
=======

Copyright © 2013 Rackspace Hosting under the [Apache 2](http://www.apache.org/licenses/LICENSE-2.0) license. See the LICENSE file for more information. 

Note: This plugin is based on Bert Frees' [xproc-maven-plugin](https://github.com/bertfrees/xproc-maven-plugin), Copyright © 2013 [Bert Frees](http://github.com/bertfrees) released under the[Apache 2](http://www.apache.org/licenses/LICENSE-2.0) license. 

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.

