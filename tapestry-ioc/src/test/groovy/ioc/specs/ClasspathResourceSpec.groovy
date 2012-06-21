package ioc.specs

import org.apache.tapestry5.ioc.internal.util.ClasspathResource
import spock.lang.Specification

class ClasspathResourceSpec extends Specification {

  static final String RESOURCE_TXT_CONTENT = "content from resource.txt";

  static final String FOLDER = "org/apache/tapestry5/ioc/internal/util";

  static final String PATH = FOLDER + "/resource.txt";
  public static final String SAME_FOLDER_RESOURCE_CONTENT = "content from same-folder resource"

  def content(resource) {
    resource.toURL().text.trim()
  }

  def "get URL of resource"() {
    def r = new ClasspathResource(PATH)

    expect:

    content(r) == RESOURCE_TXT_CONTENT
  }

  def "expend path from root resource yields same URL as full path"() {
    def r = new ClasspathResource("").forFile(PATH)

    expect:

    content(r) == RESOURCE_TXT_CONTENT
  }

  def "a leading slash is ignored for the root resource"() {
    def r = new ClasspathResource("/").forFile(PATH)

    expect:

    content(r) == RESOURCE_TXT_CONTENT
  }

  def "a leading slash is ignored for the path from the root resource"() {
    def r = new ClasspathResource("/").forFile("/" + PATH)

    expect:

    content(r) == RESOURCE_TXT_CONTENT
  }

  def "path and file for resource are available"() {
    when:

    def r = new ClasspathResource(PATH)

    then:

    r.folder == FOLDER
    r.file == "resource.txt"
  }

  def "access a file in the same folder"() {
    when:

    def r = new ClasspathResource(PATH)
    def n = r.forFile("same-folder.txt")

    then:

    content(n) == SAME_FOLDER_RESOURCE_CONTENT
  }

  def "access a file in same folder using using ./ relative path"() {
    when:

    def r = new ClasspathResource(PATH)
    def n = r.forFile("./same-folder.txt")

    then:

    content(n) == SAME_FOLDER_RESOURCE_CONTENT

  }

  def "multiple slashes treated as a single slash"() {
    when:

    def r = new ClasspathResource(PATH)
    def n = r.forFile("././/.///same-folder.txt")

    then:

    content(n) == SAME_FOLDER_RESOURCE_CONTENT

  }

  def "file in subfolder"() {
    when:

    def r = new ClasspathResource(PATH)
    def n = r.forFile("sub/sub-folder.txt")

    then:

    content(n) == "content from sub-folder resource"

  }

  def "reference same resource yields same instance"() {
    when:

    def r = new ClasspathResource(PATH)


    then:

    r.forFile("../util/resource.txt").is(r)
  }

  def "access file in parent folder"() {
    def r = new ClasspathResource(PATH)

    when:

    def n = r.forFile("../parent-folder.txt")

    then:

    content(n) == "content from parent-folder resource"
  }

  def "toString() is meaningful"() {
    expect:

    new ClasspathResource(PATH).toString() == "classpath:$PATH"
  }

  def "URL for a missing resource is null"() {
    when:

    def r = new ClasspathResource("$FOLDER/missing-resource.txt")

    then:

    r.toURL() == null
  }

  def "localization of a resource"() {

    def r = new ClasspathResource(PATH)

    when:

    def l = r.forLocale(Locale.FRENCH)

    then:

    content(l) == "french content"
  }

  def "localization finds closest match"() {
    def r = new ClasspathResource(PATH)

    when:

    def l = r.forLocale(Locale.CANADA_FRENCH)

    then:

    content(l) == "french content"
  }

  def "localization to base resource"() {
    def r = new ClasspathResource(PATH)

    when:

    def l = r.forLocale(Locale.JAPANESE)

    then:

    l.is(r)
  }

  def "localization of missing resource is null"() {
    def r = new ClasspathResource("$FOLDER/missing-resource.txt")

    expect:

    r.forLocale(Locale.KOREAN) == null

  }

  def "extending to new extension that matches old extension yields same Resource"() {
    def r = new ClasspathResource(PATH)

    when:

    def n = r.withExtension("txt")

    then:

    n.is(r)
  }

  def "extending with a new extension"() {
    def r = new ClasspathResource(PATH)

    when:

    def n = r.withExtension("ext")

    then:

    content(n) == "ext content"
  }

  def "a new extension that does not exist is a null URL"() {
    def r = new ClasspathResource(PATH)


    expect:

    r.withExtension("does-not-exist").toURL() == null
  }
}
