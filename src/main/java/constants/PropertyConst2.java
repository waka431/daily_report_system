package constants;

public enum PropertyConst2 {

    //ペッパー文字列
    PEPPER("pepper");

    private final String text;
    private PropertyConst2(final String text) {
        this.text = text;
    }

    public String getValue() {
        return this.text;
    }

}
