package it.mitl.maleficium.capability.blood;

public class BloodCapabilityImpl implements BloodCapability.IBlood {
    private float blood;
    private float maxBlood;

    @Override
    public float getBlood() { return blood; }

    @Override
    public void setBlood(float amount) {
        this.blood = Math.max(0, Math.min(amount, maxBlood));
    }

    @Override
    public float getMaxBlood() { return maxBlood; }

    @Override
    public void setMaxBlood(float amount) {
        this.maxBlood = amount;
        this.blood = Math.min(blood, maxBlood);
    }
}
