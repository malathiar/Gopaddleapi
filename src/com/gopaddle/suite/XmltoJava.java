package com.gopaddle.suite;

import java.util.Arrays;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "XmltoJava")
public class XmltoJava extends GenericClass {
     
	private Suite[] suite;
	private String name;

	@XmlElement
	public Suite[] getSuite() {
		return suite;
	}

	public void setSuite(Suite[] suite) {
		this.suite = suite;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlRootElement
	public static class Suite {
		private Parameter[] parameter;

		private String suitename;
		private String classname;

		@XmlAttribute
		public String getClassname() {
			return classname;
		}

		public void setClassname(String classname) {
			this.classname = classname;
		}

		@XmlAttribute
		public String getSuitename() {
			return suitename;
		}

		public void setSuitename(String suitename) {
			this.suitename = suitename;
		}

		@XmlElement
		public Parameter[] getParameter() {
			return parameter;
		}

		public void setParameter(Parameter[] parameter) {
			this.parameter = parameter;
		}

		@Override
		public String toString() {
			return "Suite [parameter=" + Arrays.toString(parameter) + ", suitename=" + suitename + "]";
		}

	}

	@XmlRootElement
	public static class Parameter {

		private String name;
		private String testcase;
		private String repopath;
		private String repoprovider;
		private String build;
		private String builder;
		private String startscript;
		private String postinstall;
		private String preinstall;
		private String platform;
		private String ports;
		private String designname;
		private String sourcelink;
		private String targetlink;
		private String kubname;
		private String altport;
		private String appname;
		private String accesssublink;
		private String version;
		private String path;
		private String install;
		private String auth;
		
		@XmlAttribute
		public String getAuth() {
			return auth;
		}
		public void setAuth(String auth) {
			this.auth = auth;
		}
		@XmlAttribute
		public String getInstall() {
			return install;
		}
		public void setInstall(String install) {
			this.install = install;
		}
		@XmlAttribute
		public String getVersion() {
			return version;
		}
		public void setVersion(String version) {
			this.version = version;
		}
		@XmlAttribute
		public String getPath() {
			return path;
		}
		public void setPath(String path) {
			this.path = path;
		}
		@XmlAttribute
		public String getAccesssublink() {
			return accesssublink;
		}
		public void setAccesssublink(String accesssublink) {
			this.accesssublink = accesssublink;
		}
		@XmlAttribute
		public String getAppname() {
			return appname;
		}
		public void setAppname(String appname) {
			this.appname = appname;
		}
		@XmlAttribute
		public String getAltport() {
			return altport;
		}
		public void setAltport(String altport) {
			this.altport = altport;
		}
		@XmlAttribute
		public String getKubname() {
			return kubname;
		}
		public void setKubname(String kubname) {
			this.kubname = kubname;
		}
		@XmlAttribute
		public String getSourcelink() {
			return sourcelink;
		}
		public void setSourcelink(String sourcelink) {
			this.sourcelink = sourcelink;
		}
		@XmlAttribute
		public String getTargetlink() {
			return targetlink;
		}
		public void setTargetlink(String targetlink) {
			this.targetlink = targetlink;
		}
		@XmlAttribute
		public String getDesignname() {
			return designname;
		}
		public void setDesignname(String designname) {
			this.designname = designname;
		}
		@XmlAttribute
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		@XmlAttribute
		public String getTestcase() {
			return testcase;
		}
		public void setTestcase(String testcase) {
			this.testcase = testcase;
		}
		@XmlAttribute
		public String getRepopath() {
			return repopath;
		}
		public void setRepopath(String repopath) {
			this.repopath = repopath;
		}
		@XmlAttribute
		public String getRepoprovider() {
			return repoprovider;
		}
		public void setRepoprovider(String repoprovider) {
			this.repoprovider = repoprovider;
		}
		
		@XmlAttribute
		public String getBuild() {
			return build;
		}
		public void setBuild(String build) {
			this.build = build;
		}
		@XmlAttribute
		public String getBuilder() {
			return builder;
		}
		public void setBuilder(String builder) {
			this.builder = builder;
		}
		@XmlAttribute
		public String getStartscript() {
			return startscript;
		}
		public void setStartscript(String start_script) {
			this.startscript = start_script;
		}
		@XmlAttribute
		public String getPostinstall() {
			return postinstall;
		}
		public void setPostinstall(String post_install) {
			this.postinstall = post_install;
		}
		@XmlAttribute
		public String getPreinstall() {
			return preinstall;
		}
		public void setPreinstall(String pre_install) {
			this.preinstall = pre_install;
		}
		@XmlAttribute
		public String getPlatform() {
			return platform;
		}
		public void setPlatform(String platform) {
			this.platform = platform;
		}
		@XmlAttribute
		public String getPorts() {
			return ports;
		}
		public void setPorts(String ports) {
			this.ports = ports;
		}
		@Override
		public String toString() {
			return "Parameter [name=" + name + ", testcase=" + testcase + ", repopath=" + repopath + ", repoprovider="
					+ repoprovider + ", build=" + build + ", builder=" + builder + ", startscript=" + startscript
					+ ", postinstall=" + postinstall + ", preinstall=" + preinstall + ", platform=" + platform
					+ ", ports=" + ports + ", designname=" + designname + ", sourcelink=" + sourcelink + ", targetlink="
					+ targetlink + ", kubname=" + kubname + ", altport=" + altport + ", appname=" + appname
					+ ", accesssublink=" + accesssublink + ", version=" + version + ", path=" + path + ", install="
					+ install + ", auth=" + auth + "]";
		}
		
		
		
		
		
		
		
		

	}

	@Override
	public String toString() {
		return "XmltoJava [suite=" + Arrays.toString(suite) + ", name=" + name + "]";
	}

}
