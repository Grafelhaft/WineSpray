package de.grafelhaft.winespray.app.util;

import java.util.Random;

import de.grafelhaft.winespray.app.R;

/**
 * Created by Markus on 15.09.2016.
 */
public class ResourceUtils {

    private static int[] _imgs = {
            R.drawable.img_vineyard_1,
            R.drawable.img_vineyard_2,
            R.drawable.img_vineyard_3,
            R.drawable.img_vineyard_4,
            R.drawable.img_vineyard_5,
            R.drawable.img_vineyard_6,
            R.drawable.img_vineyard_7,
    };


    public static int getVineyardRes(long position) {
        int pos = (int) (position % _imgs.length);
        return _imgs[pos];
    }


    private static String[] _quotes = {
            "Winter is coming",
            "They may take our lives, but they'll never take our freedom!",
            "Chewie, we're home.",
            "They call it a Royale with cheese.",
            "Yo, Adrian!",
            "Wax on, wax off.",
            "Help me, Obi-Wan Kenobi. You're my only hope.",
            "I'm having an old friend for dinner.",
            "Just keep swimming.",
            "Shaken, not stirred.",
            "Keep your friends close, but your enemies closer.",
            "Roads? Where we're going we don't need roads.",
            "Carpe diem. Seize the day, boys.",
            "To infinity and beyond!",
            "Why so serious?",
            "The first rule of Fight Club is: You do not talk about Fight Club.",
            "I'm going to make him an offer he can't refuse.",
    };

    public static String getQuote() {
        return _quotes[new Random().nextInt(_quotes.length)];
    }
}
