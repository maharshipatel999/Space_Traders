package spacetrader;

public abstract class PoliticalSystem {
	// percent, meaning percent increase from the norm
	double policePercent, piratePercent, traderPercent, bribePercent, generalPriceIncreasePercent, highTechPriceIncreasePercent, essentialGoodsPriceIncreasePercent;
	boolean isForbiddenGoods;
	String system;
	
	public double getPoliceMultiplier(){
		return policePercent + 1;
	}
	public double getPirateMultiplier(){
		return piratePercent + 1;
	}
	public double getTraderMultiplier(){
		return traderPercent + 1;
	}
	public double getBribeMultiplier(){
		return bribePercent + 1;
	}
	public boolean isForbidden(){
		return isForbiddenGoods;
	}
	public double getGeneralPriceIncreasePercent(){
		return generalPriceIncreasePercent + 1;
	}
	
	public double getHighTechPriceIncreasePercent(){
		return highTechPriceIncreasePercent + 1;
	}
	
	public double getEssentialGoodsPriceIncreasePercent(){
		return essentialGoodsPriceIncreasePercent + 1;
	}

}