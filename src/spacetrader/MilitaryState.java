package spacetrader;

public class MilitaryState extends PoliticalSystem{

	public MilitaryState() {
		system = "Military State";
		policePercent = 1.0;
		piratePercent = 0.0;
		traderPercent = 0.8;
		bribePercent = 0.6;
		generalPriceIncreasePercent = 1.4;
		highTechPriceIncreasePercent = 1.0;
		essentialGoodsPriceIncreasePercent = 0;
		isForbiddenGoods = false;
	}

}