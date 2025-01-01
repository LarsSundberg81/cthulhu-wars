package cws

import colmat._

case object Undead extends FactionUnitClass(YS, "Undead", Monster, 1)
case object Byakhee extends FactionUnitClass(YS, "Byakhee", Monster, 2)
case object KingInYellow extends FactionUnitClass(YS, "King in Yellow", GOO, 4)
case object Hastur extends FactionUnitClass(YS, "Hastur", GOO, 10)

case object Feast extends FactionSpellbook(YS, "Feast")
case object Desecrate extends FactionSpellbook(YS, "Desecrate")
case object Vengeance extends FactionSpellbook(YS, "Vengeance")

case object HWINTBN extends FactionSpellbook(YS, "He Who is Not to be Named")
case object ThirdEye extends FactionSpellbook(YS, "The Third Eye")
case object Shriek extends FactionSpellbook(YS, "Shriek of the Byakhee")
case object Zingaya extends FactionSpellbook(YS, "Zingaya")
case object ScreamingDead extends FactionSpellbook(YS, "The Screaming Dead")
case object Passion extends FactionSpellbook(YS, "Passion")


case object Provide3Doom extends Requirement("Provide 3 Doom")
case object DesecrateWW extends Requirement("Desecrate an Area with ||| Glyph")
case object DesecrateOO extends Requirement("Desecrate an Area with (*) Glyph")
case object DesecrateAA extends Requirement("Desecrate an Area with /^\\ Glyph")
case object AwakenKing extends Requirement("Awaken King in Yellow")
case object AwakenHastur extends Requirement("Awaken Hastur", 1)


case object YS extends Faction {
    def name = "Yellow Sign"
    def short = "YS"
    def style = "ys"
    val poolR = Region(name + " Pool", Pool)
    val prison = Region(name + " Prison", Prison)

    override def abilities : List[Spellbook] = List(Feast, Desecrate, Vengeance)
    override def spellbooks : List[Spellbook] = List(Passion, Zingaya, Shriek, ScreamingDead, ThirdEye, HWINTBN)
    override def requirements : List[Requirement] = List(Provide3Doom, DesecrateWW, DesecrateOO, DesecrateAA, AwakenKing, AwakenHastur)

    val allUnits =
        List.fill(1)(Hastur) ++
        List.fill(1)(KingInYellow) ++
        List.fill(4)(Byakhee) ++
        List.fill(6)(Undead) ++
        List.fill(6)(Acolyte)

    override def awakenCost(g : Game, u : UnitClass, r : Region) = u match {
        case KingInYellow => (!g.gates.contains(r) && !g.ugates.contains(r) && g.of(this).at(r).any).?(4).|(999)
        case Hastur => (g.of(this).gates.contains(r) && g.of(this).at(r, KingInYellow).any).?(10).|(999)
    }

    override def strength(g : Game, units : List[UnitFigure], opponent : Faction) =
        units.count(_.uclass == Undead) * 1 - units.exists(_.uclass == Undead).?(1).|(0) +
        units.count(_.uclass == Byakhee) * 1 + units.exists(_.uclass == Byakhee).?(1).|(0) +
        units.count(_.uclass == Hastur) * g.ritualCost

}
