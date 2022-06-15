package fr.dila.solon.birt.common;

public enum BirtOutputFormat {
	HTML, XLSX, XLS, DOCX, DOC, PDF;

	public String getExtension() {
		return name().toLowerCase();
	}

	public String getExtensionWithSeparator() {
		return '.' + getExtension();
	}
}