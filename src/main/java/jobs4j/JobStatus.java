package jobs4j;

public enum JobStatus {
	NEW("New"), SUBMITTED("Submitted"), ACTIVE("Active"), COMPLETED("Completed"), ERROR("Error");

	private String label;

	JobStatus(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return label;
	}
}
