package chrysalis.item.slingShot.ammo;

public enum ShootingType {
  NONE(1),
  SLING_SHOT(2.5),
  THROWN(1);

  private final double damageMultiplier;

  ShootingType(double damageMultiplier) {
    this.damageMultiplier = damageMultiplier;
  }

  public double getDamageMultiplier() {
    return this.damageMultiplier;
  }

}
