package stat1kDev.quizapp.models.quiz.quiz;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class QuizModel implements Parcelable {
    String question;
    ArrayList<String> answers;
    int correctAnswer;
    String questinCategoryId;
    ArrayList<String> backgroundColors;

    public QuizModel(String question, ArrayList<String> answers, int correctAnswer, String questinCategoryId, ArrayList<String> backgroundColors) {
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.answers = answers;
        this.questinCategoryId = questinCategoryId;
        this.backgroundColors = backgroundColors;
    }

    public String getQuestion() {
        return question;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public String getQuestingCategoryId() {
        return questinCategoryId;
    }

    public void setBackgroundColors(ArrayList<String> backgroundColors) {
        this.backgroundColors = backgroundColors;
    }

    public ArrayList<String> getBackgroundColors() {
        return backgroundColors;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(question);
        dest.writeList(answers);
        dest.writeInt(correctAnswer);
        dest.writeString(questinCategoryId);
        dest.writeList(backgroundColors);
    }

    protected QuizModel(Parcel in) {
        question = in.readString();
        in.readList(answers, QuizModel.class.getClassLoader());
        correctAnswer = in.readInt();
        questinCategoryId = in.readString();
        in.readList(backgroundColors, QuizModel.class.getClassLoader());
    }

    public static Creator<QuizModel> getCREATOR() {
        return CREATOR;
    }

    public static final Creator<QuizModel> CREATOR = new Creator<QuizModel>() {
        @Override
        public QuizModel createFromParcel(Parcel source) {
            return new QuizModel(source);
        }

        @Override
        public QuizModel[] newArray(int size) {
            return new QuizModel[size];
        }
    };

}