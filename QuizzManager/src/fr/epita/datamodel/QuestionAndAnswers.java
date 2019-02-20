package fr.epita.datamodel;

import java.util.List;

public class QuestionAndAnswers {
	private int id;

	private String question;

	private List<String> answers;

	private int difficulty;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	

	public List<String> getAnswers() {
		return answers;
	}

	public void setAnswers(List<String> answers) {
		this.answers = answers;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		QuestionAndAnswers other = (QuestionAndAnswers) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "QuestionAndAnswers [id=" + id + ", question=" + question + ", answers=" + answers + ", difficulty="
				+ difficulty + "]";
	}
	
	
	

}
