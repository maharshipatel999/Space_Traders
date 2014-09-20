package spacetrader;

public class Anarchy extends PoliticalSystem{
	
	public Anarchy() {
		system = "Anarchy";
		policePercent = 0.05;
		piratePercent = 0.8;
		traderPercent = 0.05;
		bribePercent = 1.0;
		generalPriceIncreasePercent = 1.0;
		highTechPriceIncreasePercent = 0;
		essentialGoodsPriceIncreasePercent = 0;
		isForbiddenGoods = false;
	}

}