public class Pokemon {

    //Stats to be collected from data
    int poke_id;
    String poke_name;
    Poke_Type PrimaryType;
    Poke_Type SecondaryType;
    double baseHp;
    int attack;
    int sp_attack;
    int defense;
    int sp_defense;
    public Pokemon() {

    }

    public int getPoke_id() {
        return poke_id;
    }

    public void setPoke_id(int poke_id) {
        this.poke_id = poke_id;
    }

    public String getPoke_name() {
        return poke_name;
    }

    public void setPoke_name(String poke_name) {
        this.poke_name = poke_name;
    }

    public Poke_Type getPrimaryType() {
        return PrimaryType;
    }

    public void setPrimaryType(Poke_Type primaryType) {
        PrimaryType = primaryType;
    }

    public Poke_Type getSecondaryType() {
        return SecondaryType;
    }

    public void setSecondaryType(Poke_Type secondaryType) {
        SecondaryType = secondaryType;
    }

    public double getBaseHp() {
        return baseHp;
    }

    public void setBaseHp(double baseHp) {
        this.baseHp = baseHp;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getSp_attack() {
        return sp_attack;
    }

    public void setSp_attack(int sp_attack) {
        this.sp_attack = sp_attack;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getSp_defense() {
        return sp_defense;
    }

    public void setSp_defense(int sp_defense) {
        this.sp_defense = sp_defense;
    }
}
