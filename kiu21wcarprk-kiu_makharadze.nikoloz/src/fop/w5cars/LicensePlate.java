package fop.w5cars;

import java.util.Objects;

public class LicensePlate {
	// TODO
    private String regionalCode;
    private String letters;
    private int digits;

    public String getRegionalCode() {
        return regionalCode;
    }


    public String getLetters() {
        return letters;
    }

    public int getDigits() {
        return digits;
    }

    public void setRegionalCode(String regionalCode) {
        this.regionalCode = regionalCode;
    }

    public void setLetters(String letters) {
        this.letters = letters;
    }

    public void setDigits(int digits) {
        this.digits = digits;
    }

    public LicensePlate(String regionalCode, String letters, int digits){
        this.regionalCode=regionalCode;
        this.letters=letters;
        this.digits=digits;

    }
    public boolean isEqual(LicensePlate other){
        return this.regionalCode.equals(other.regionalCode)&&this.letters.equals(other.letters)&&this.digits== other.digits;
    }

    @Override
    public String toString() {
        return  this.regionalCode + ":"  + this.letters + ' ' +
                  this.digits;
    }
}
