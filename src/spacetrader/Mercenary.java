/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader;

/**
 * Represents a Mercenary the player can hire and add to his ship's crew.
 *
 * @author Caleb
 */
public class Mercenary extends Trader {

    /**
     * Constructor for Mercenary
     *
     * @param name - name of the mercenary
     * @param pilot - pilot skill of mercenary
     * @param fighter - fighter skill of mercenary
     * @param trader - trader skill of mercenary
     * @param engineer - engineer skill of mercenary
     * @param investor - investor skill of mercenary
     */
    public Mercenary(String name, int pilot, int fighter, int trader, int engineer, int investor) {
        super(name, pilot, fighter, trader, engineer, investor);
    }

}
