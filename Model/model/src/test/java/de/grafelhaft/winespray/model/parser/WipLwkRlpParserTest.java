package de.grafelhaft.winespray.model.parser;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by @author Markus Graf (Grafelhaft) on 12.10.2016.
 */
public class WipLwkRlpParserTest {

    private static String INPUT = "ERNTEJAHR;ERSTELLT;BETRNR;GMK_NR;GEMARKUNG;MATCHCODE;FLUR;Z;N;FL_EJBIS;FL_EJAB;FL_STAT;FL_AKT;FL_ART;BA;BER;GL;EL;LAGE_NR;BA_NAME;BER_NAME;GL_NAME;EL_NAME;FLB_B;VKZNR;FLB_NAME;WBR_H_F;ABG_ST;ABG_STST;FA_EJAB;FA_EJBIS;FA_STAT;NA_ID;NUTZUNGSART;REB_ID;REB_NAME;FARBE;KATEG;REBSTAT_ID;REBSTAT_NAME;UNL_ID;UNTERLAGE;RP_DATUM;FLA_QM;FLU_QM;ERZ_ID;ERZIEHUNGSART;BETRNR_EZZ;BF;BESITZFORM;GEN;VERSGEN\n" +
            "2016;06.10.2016;333452;3908;Horchheim;HORCHHEI;1;352;0;2016;;;A;A;7;3;05;05;730505;Rheinhessen;Bereich Wonnegau;Liebfrauenmorgen;Goldberg;;;;F;N;N;2016;;A;1;bestockt;035;Müller-Thurgau;weiß;K;035;Müller-Thurgau;902;125 AA;01.05.2016;1561;1561;;;;;;J;\n" +
            "2016;06.10.2016;333452;3908;Horchheim;HORCHHEI;1;353;1;2016;;;A;A;7;3;05;05;730505;Rheinhessen;Bereich Wonnegau;Liebfrauenmorgen;Goldberg;;;;F;N;N;2016;;A;1;bestockt;035;Müller-Thurgau;weiß;K;035;Müller-Thurgau;902;125 AA;01.05.2016;1050;1050;;;;;;J;\n" +
            "2016;06.10.2016;333452;3908;Horchheim;HORCHHEI;1;354;0;2016;;;A;A;7;3;05;05;730505;Rheinhessen;Bereich Wonnegau;Liebfrauenmorgen;Goldberg;;;;F;N;N;2016;;A;1;bestockt;035;Müller-Thurgau;weiß;K;035;Müller-Thurgau;908;SO 4;01.04.2014;1131;1131;;;;;;J;\n" +
            "2016;06.10.2016;333452;3908;Horchheim;HORCHHEI;1;355;0;2016;;;A;A;7;3;05;05;730505;Rheinhessen;Bereich Wonnegau;Liebfrauenmorgen;Goldberg;;;;F;N;N;2016;;A;1;bestockt;035;Müller-Thurgau;weiß;K;035;Müller-Thurgau;908;SO 4;01.04.2014;967;967;;;;;;J;\n" +
            "2016;06.10.2016;333452;3908;Horchheim;HORCHHEI;1;356;0;2016;;;A;A;7;3;05;05;730505;Rheinhessen;Bereich Wonnegau;Liebfrauenmorgen;Goldberg;;;;F;N;N;2016;;A;1;bestockt;035;Müller-Thurgau;weiß;K;035;Müller-Thurgau;908;SO 4;01.04.2014;506;506;;;;;;J;\n" +
            "2016;06.10.2016;333452;3908;Horchheim;HORCHHEI;1;357;0;2016;;;A;A;7;3;05;05;730505;Rheinhessen;Bereich Wonnegau;Liebfrauenmorgen;Goldberg;;;;F;N;N;2016;;A;1;bestockt;035;Müller-Thurgau;weiß;K;035;Müller-Thurgau;908;SO 4;01.04.2014;1204;1204;;;;;;J;\n" +
            "2016;06.10.2016;333452;3908;Horchheim;HORCHHEI;1;358;0;2016;;;A;A;7;3;05;05;730505;Rheinhessen;Bereich Wonnegau;Liebfrauenmorgen;Goldberg;;;;F;N;N;2016;;A;1;bestockt;035;Müller-Thurgau;weiß;K;035;Müller-Thurgau;908;SO 4;01.04.2014;3413;3413;;;;;;J;\n" +
            "2016;06.10.2016;333452;3908;Horchheim;HORCHHEI;1;359;0;2016;;;A;A;7;3;05;05;730505;Rheinhessen;Bereich Wonnegau;Liebfrauenmorgen;Goldberg;;;;F;N;N;2016;;A;1;bestockt;035;Müller-Thurgau;weiß;K;035;Müller-Thurgau;908;SO 4;01.04.2014;534;534;;;;;;J;\n" +
            "2016;06.10.2016;333452;3908;Horchheim;HORCHHEI;1;360;0;2016;;;A;A;7;3;05;05;730505;Rheinhessen;Bereich Wonnegau;Liebfrauenmorgen;Goldberg;;;;F;N;N;2016;;A;1;bestockt;035;Müller-Thurgau;weiß;K;035;Müller-Thurgau;908;SO 4;01.04.2014;546;546;;;;;;J;\n" +
            "2016;06.10.2016;333452;3908;Horchheim;HORCHHEI;1;361;0;2016;;;A;A;7;3;05;05;730505;Rheinhessen;Bereich Wonnegau;Liebfrauenmorgen;Goldberg;;;;F;N;N;2016;;A;1;bestockt;035;Müller-Thurgau;weiß;K;035;Müller-Thurgau;908;SO 4;01.04.2014;779;779;;;;;;J;\n" +
            "2016;06.10.2016;333452;3908;Horchheim;HORCHHEI;1;382;2;2016;;;A;A;7;3;05;05;730505;Rheinhessen;Bereich Wonnegau;Liebfrauenmorgen;Goldberg;;;;F;N;N;2016;;A;2;gerodet, nicht bestockt;;;;;;;;;01.11.2008;536;536;;;;;;J;\n" +
            "2016;06.10.2016;333452;3908;Horchheim;HORCHHEI;1;383;2;2016;;;A;A;7;3;05;05;730505;Rheinhessen;Bereich Wonnegau;Liebfrauenmorgen;Goldberg;;;;F;N;N;2016;;A;2;gerodet, nicht bestockt;;;;;;;;;01.11.2008;415;415;;;;;;J;\n" +
            "2016;06.10.2016;333452;3908;Horchheim;HORCHHEI;1;384;2;2016;;;A;A;7;3;05;05;730505;Rheinhessen;Bereich Wonnegau;Liebfrauenmorgen;Goldberg;;;;F;N;N;2016;;A;2;gerodet, nicht bestockt;;;;;;;;;01.11.2008;386;386;;;;;;J;\n" +
            "2016;06.10.2016;333452;3908;Horchheim;HORCHHEI;1;385;2;2016;;;A;A;7;3;05;05;730505;Rheinhessen;Bereich Wonnegau;Liebfrauenmorgen;Goldberg;;;;F;N;N;2016;;A;2;gerodet, nicht bestockt;;;;;;;;;01.02.2011;617;617;;;;;;J;\n" +
            "2016;06.10.2016;333452;3908;Horchheim;HORCHHEI;1;386;2;2016;;;A;A;7;3;05;05;730505;Rheinhessen;Bereich Wonnegau;Liebfrauenmorgen;Goldberg;;;;F;N;N;2016;;A;2;gerodet, nicht bestockt;;;;;;;;;01.02.2011;292;292;;;;;;J;\n" +
            "2016;06.10.2016;333452;3908;Horchheim;HORCHHEI;1;678;0;2016;;;A;A;7;3;05;05;730505;Rheinhessen;Bereich Wonnegau;Liebfrauenmorgen;Goldberg;;;;F;N;N;2016;;A;1;bestockt;068;Weißer Burgunder;weiß;K;068;Weißer Burgunder;908;SO 4;01.04.2010;1373;1373;;;;;;J;\n" +
            "2016;06.10.2016;333452;3908;Horchheim;HORCHHEI;1;679;0;2016;;;A;A;7;3;05;05;730505;Rheinhessen;Bereich Wonnegau;Liebfrauenmorgen;Goldberg;;;;F;N;N;2016;;A;1;bestockt;068;Weißer Burgunder;weiß;K;068;Weißer Burgunder;908;SO 4;01.04.2010;1794;1794;;;;;;J;\n" +
            "2016;06.10.2016;333452;3908;Horchheim;HORCHHEI;2;236;0;2016;;;A;A;7;3;05;05;730505;Rheinhessen;Bereich Wonnegau;Liebfrauenmorgen;Goldberg;;;;F;N;N;2016;;A;2;gerodet, nicht bestockt;;;;;;;;;01.11.2014;325;1225;;;;;;J;\n" +
            "2016;06.10.2016;333452;3908;Horchheim;HORCHHEI;2;236;0;2016;;;A;A;7;3;05;05;730505;Rheinhessen;Bereich Wonnegau;Liebfrauenmorgen;Goldberg;;;;F;N;N;2016;;A;2;gerodet, nicht bestockt;;;;;;;;;01.11.1993;900;1225;;;;;;J;\n" +
            "2016;06.10.2016;333452;3908;Horchheim;HORCHHEI;2;254;0;2016;;;A;A;7;3;05;05;730505;Rheinhessen;Bereich Wonnegau;Liebfrauenmorgen;Goldberg;;;;F;N;N;2016;;A;1;bestockt;104;Dornfelder;rot;K;104;Dornfelder;908;SO 4;01.04.1998;2375;2375;;;;;;J;\n" +
            "2016;06.10.2016;333452;3908;Horchheim;HORCHHEI;3;42;0;2016;;;A;A;7;3;05;05;730505;Rheinhessen;Bereich Wonnegau;Liebfrauenmorgen;Goldberg;;;;F;N;N;2016;;A;1;bestockt;104;Dornfelder;rot;K;104;Dornfelder;908;SO 4;01.04.2007;2053;2053;;;;;;J;\n" +
            "2016;06.10.2016;333452;3908;Horchheim;HORCHHEI;3;43;0;2016;;;A;A;7;3;05;05;730505;Rheinhessen;Bereich Wonnegau;Liebfrauenmorgen;Goldberg;;;;F;N;N;2016;;A;1;bestockt;104;Dornfelder;rot;K;104;Dornfelder;908;SO 4;01.04.2007;1721;1721;;;;;;J;\n" +
            "2016;06.10.2016;333452;3908;Horchheim;HORCHHEI;3;44;0;2016;;;A;A;7;3;05;05;730505;Rheinhessen;Bereich Wonnegau;Liebfrauenmorgen;Goldberg;;;;F;N;N;2016;;A;1;bestockt;104;Dornfelder;rot;K;104;Dornfelder;908;SO 4;01.04.2007;1016;1016;;;;;;J;\n" +
            "2016;06.10.2016;333452;3908;Horchheim;HORCHHEI;3;45;0;2016;;;A;A;7;3;05;05;730505;Rheinhessen;Bereich Wonnegau;Liebfrauenmorgen;Goldberg;;;;F;N;N;2016;;A;1;bestockt;104;Dornfelder;rot;K;104;Dornfelder;908;SO 4;01.04.2007;1737;1737;;;;;;J;\n" +
            "2016;06.10.2016;333452;3908;Horchheim;HORCHHEI;3;49;0;2016;;;A;A;7;3;05;05;730505;Rheinhessen;Bereich Wonnegau;Liebfrauenmorgen;Goldberg;;;;F;N;N;2016;;A;1;bestockt;122;Cabernet Sauvignon;rot;K;122;Cabernet Sauvignon;908;SO 4;01.04.2012;2358;2358;;;;;;J;\n" +
            "2016;06.10.2016;333452;3908;Horchheim;HORCHHEI;3;50;0;2016;;;A;A;7;3;05;05;730505;Rheinhessen;Bereich Wonnegau;Liebfrauenmorgen;Goldberg;;;;F;N;N;2016;;A;1;bestockt;122;Cabernet Sauvignon;rot;K;122;Cabernet Sauvignon;908;SO 4;01.03.2004;1677;1677;;;;;;J;\n" +
            "2016;06.10.2016;333452;3908;Horchheim;HORCHHEI;3;51;0;2016;;;A;A;7;3;05;05;730505;Rheinhessen;Bereich Wonnegau;Liebfrauenmorgen;Goldberg;;;;F;N;N;2016;;A;1;bestockt;122;Cabernet Sauvignon;rot;K;122;Cabernet Sauvignon;908;SO 4;01.04.2011;974;974;;;;;;J;\n" +
            "2016;06.10.2016;333452;3908;Horchheim;HORCHHEI;3;72;0;2016;;;A;A;7;3;05;05;730505;Rheinhessen;Bereich Wonnegau;Liebfrauenmorgen;Goldberg;;;;F;N;N;2016;;A;2;gerodet, nicht bestockt;;;;;;;;;01.02.2011;468;468;;;;;;J;\n" +
            "2016;06.10.2016;333452;3908;Horchheim;HORCHHEI;3;73;0;2016;;;A;A;7;3;05;05;730505;Rheinhessen;Bereich Wonnegau;Liebfrauenmorgen;Goldberg;;;;F;N;N;2016;;A;2;gerodet, nicht bestockt;;;;;;;;;01.04.2016;1159;1159;;;;;;J;\n" +
            "2016;06.10.2016;333452;3908;Horchheim;HORCHHEI;3;74;0;2016;;;A;A;7;3;05;05;730505;Rheinhessen;Bereich Wonnegau;Liebfrauenmorgen;Goldberg;;;;F;N;N;2016;;A;2;gerodet, nicht bestockt;;;;;;;;;01.04.2016;726;726;;;;;;J;\n" +
            "2016;06.10.2016;333452;3908;Horchheim;HORCHHEI;3;85;0;2016;;;A;A;7;3;05;05;730505;Rheinhessen;Bereich Wonnegau;Liebfrauenmorgen;Goldberg;;;;F;N;N;2016;;A;1;bestockt;035;Müller-Thurgau;weiß;K;035;Müller-Thurgau;908;SO 4;01.04.2006;1205;1205;;;;;;J;\n" +
            "2016;06.10.2016;333452;3908;Horchheim;HORCHHEI;3;86;0;2016;;;A;A;7;3;05;05;730505;Rheinhessen;Bereich Wonnegau;Liebfrauenmorgen;Goldberg;;;;F;N;N;2016;;A;1;bestockt;035;Müller-Thurgau;weiß;K;035;Müller-Thurgau;908;SO 4;01.04.2006;1130;1130;;;;;;J;\n" +
            "2016;06.10.2016;333452;3908;Horchheim;HORCHHEI;3;87;0;2016;;;A;A;7;3;05;05;730505;Rheinhessen;Bereich Wonnegau;Liebfrauenmorgen;Goldberg;;;;F;N;N;2016;;A;1;bestockt;035;Müller-Thurgau;weiß;K;035;Müller-Thurgau;908;SO 4;01.04.2006;729;729;;;;;;J;\n" +
            "2016;06.10.2016;333452;3908;Horchheim;HORCHHEI;3;89;0;2016;;;A;A;7;3;05;05;730505;Rheinhessen;Bereich Wonnegau;Liebfrauenmorgen;Goldberg;;;;F;N;N;2016;;A;1;bestockt;072;Chardonnay;weiß;K;072;Chardonnay;908;SO 4;01.04.2004;2163;2163;;;;;;J;\n" +
            "2016;06.10.2016;333452;3908;Horchheim;HORCHHEI;3;90;0;2016;;;A;A;7;3;05;05;730505;Rheinhessen;Bereich Wonnegau;Liebfrauenmorgen;Goldberg;;;;F;N;N;2016;;A;1;bestockt;072;Chardonnay;weiß;K;072;Chardonnay;908;SO 4;01.04.2012;2016;2016;;;;;;J;\n" +
            "2016;06.10.2016;333452;3908;Horchheim;HORCHHEI;3;91;0;2016;;;A;A;7;3;05;05;730505;Rheinhessen;Bereich Wonnegau;Liebfrauenmorgen;Goldberg;;;;F;N;N;2016;;A;1;bestockt;055;Ruländer;weiß;K;055;Ruländer;910;Binova;01.04.2005;1669;1669;;;;;;J;\n" +
            "2016;06.10.2016;333452;3908;Horchheim;HORCHHEI;3;92;1;2016;;;A;A;7;3;05;05;730505;Rheinhessen;Bereich Wonnegau;Liebfrauenmorgen;Goldberg;;;;F;N;N;2016;;A;1;bestockt;055;Ruländer;weiß;K;055;Ruländer;910;Binova;01.04.2005;6902;6902;;;;;;J;\n" +
            "2016;06.10.2016;333452;3908;Horchheim;HORCHHEI;3;104;0;2016;;;A;A;7;3;05;05;730505;Rheinhessen;Bereich Wonnegau;Liebfrauenmorgen;Goldberg;;;;F;N;N;2016;;A;1;bestockt;053;Riesling;weiß;K;053;Riesling;908;SO 4;01.04.1991;1126;1126;;;;;;J;\n" +
            "2016;06.10.2016;333452;3908;Horchheim;HORCHHEI;3;105;0;2016;;;A;A;7;3;05;05;730505;Rheinhessen;Bereich Wonnegau;Liebfrauenmorgen;Goldberg;;;;F;N;N;2016;;A;1;bestockt;053;Riesling;weiß;K;053;Riesling;908;SO 4;01.04.1991;1367;1367;;;;;;J;\n" +
            "2016;06.10.2016;333452;3908;Horchheim;HORCHHEI;3;106;0;2016;;;A;A;7;3;05;05;730505;Rheinhessen;Bereich Wonnegau;Liebfrauenmorgen;Goldberg;;;;F;N;N;2016;;A;1;bestockt;118;Spätburgunder;rot;K;118;Spätburgunder;908;SO 4;01.04.2007;2874;2874;;;;;;J;\n" +
            "2016;06.10.2016;333452;3908;Horchheim;HORCHHEI;3;107;0;2016;;;A;A;7;3;05;05;730505;Rheinhessen;Bereich Wonnegau;Liebfrauenmorgen;Goldberg;;;;F;N;N;2016;;A;1;bestockt;118;Spätburgunder;rot;K;118;Spätburgunder;908;SO 4;01.04.2007;2023;2023;;;;;;J;\n" +
            "2016;06.10.2016;333452;3908;Horchheim;HORCHHEI;3;125;0;2016;;;A;A;7;3;05;05;730505;Rheinhessen;Bereich Wonnegau;Liebfrauenmorgen;Goldberg;;;;F;N;N;2016;;A;1;bestockt;053;Riesling;weiß;K;053;Riesling;908;SO 4;01.04.2006;3302;3302;;;;;;J;\n" +
            "2016;06.10.2016;333452;3908;Horchheim;HORCHHEI;5;134;0;2016;;;A;A;7;3;05;05;730505;Rheinhessen;Bereich Wonnegau;Liebfrauenmorgen;Goldberg;;;;F;N;N;2016;;A;1;bestockt;104;Dornfelder;rot;K;104;Dornfelder;901;5 BB;01.01.1983;686;686;;;;;;J;\n" +
            "2016;06.10.2016;333452;3908;Horchheim;HORCHHEI;5;166;0;2016;;;A;A;7;3;05;05;730505;Rheinhessen;Bereich Wonnegau;Liebfrauenmorgen;Goldberg;;;;F;N;N;2016;;A;1;bestockt;104;Dornfelder;rot;K;104;Dornfelder;901;5 BB;01.01.1983;727;727;;;;;;J;\n" +
            "2016;06.10.2016;333452;3908;Horchheim;HORCHHEI;5;200;0;2016;;;A;A;7;3;05;05;730505;Rheinhessen;Bereich Wonnegau;Liebfrauenmorgen;Goldberg;;;;F;N;N;2016;;A;1;bestockt;035;Müller-Thurgau;weiß;K;035;Müller-Thurgau;908;SO 4;01.04.2015;2436;2436;;;;;;J;\n" +
            "2016;06.10.2016;333452;3908;Horchheim;HORCHHEI;5;201;0;2016;;;A;A;7;3;05;05;730505;Rheinhessen;Bereich Wonnegau;Liebfrauenmorgen;Goldberg;;;;F;N;N;2016;;A;1;bestockt;035;Müller-Thurgau;weiß;K;035;Müller-Thurgau;908;SO 4;01.04.2015;2475;2475;;;;;;J;\n" +
            "2016;06.10.2016;333452;3908;Horchheim;HORCHHEI;5;202;0;2016;;;A;A;7;3;05;05;730505;Rheinhessen;Bereich Wonnegau;Liebfrauenmorgen;Goldberg;;;;F;N;N;2016;;A;1;bestockt;035;Müller-Thurgau;weiß;K;035;Müller-Thurgau;908;SO 4;01.04.2015;818;818;;;;;;J;\n" +
            "2016;06.10.2016;333452;3908;Horchheim;HORCHHEI;5;203;0;2016;;;A;A;7;3;05;05;730505;Rheinhessen;Bereich Wonnegau;Liebfrauenmorgen;Goldberg;;;;F;N;N;2016;;A;1;bestockt;035;Müller-Thurgau;weiß;K;035;Müller-Thurgau;908;SO 4;01.04.2015;1279;1279;;;;;;J;\n" +
            "2016;06.10.2016;333452;3908;Horchheim;HORCHHEI;6;136;0;2016;;;A;A;7;3;05;05;730505;Rheinhessen;Bereich Wonnegau;Liebfrauenmorgen;Goldberg;;;;F;N;N;2016;;A;1;bestockt;053;Riesling;weiß;K;053;Riesling;908;SO 4;01.04.2008;1885;1885;;;;;;J;\n" +
            "2016;06.10.2016;333452;3908;Horchheim;HORCHHEI;6;154;0;2016;;;A;A;7;3;05;05;730505;Rheinhessen;Bereich Wonnegau;Liebfrauenmorgen;Goldberg;;;;F;N;N;2016;;A;1;bestockt;104;Dornfelder;rot;K;104;Dornfelder;908;SO 4;01.04.2011;2096;2096;;;;;;J;\n" +
            "2016;06.10.2016;333452;3908;Horchheim;HORCHHEI;6;155;0;2016;;;A;A;7;3;05;05;730505;Rheinhessen;Bereich Wonnegau;Liebfrauenmorgen;Goldberg;;;;F;N;N;2016;;A;1;bestockt;104;Dornfelder;rot;K;104;Dornfelder;908;SO 4;01.04.2011;866;866;;;;;;J;\n" +
            "2016;06.10.2016;333452;3908;Horchheim;HORCHHEI;6;159;0;2016;;;A;A;7;3;05;05;730505;Rheinhessen;Bereich Wonnegau;Liebfrauenmorgen;Goldberg;;;;F;N;N;2016;;A;1;bestockt;104;Dornfelder;rot;K;104;Dornfelder;908;SO 4;01.04.2011;1319;1319;;;;;;J;\n" +
            "2016;06.10.2016;333452;3908;Horchheim;HORCHHEI;6;160;0;2016;;;A;A;7;3;05;05;730505;Rheinhessen;Bereich Wonnegau;Liebfrauenmorgen;Goldberg;;;;F;N;N;2016;;A;1;bestockt;104;Dornfelder;rot;K;104;Dornfelder;901;5 BB;01.04.1999;2489;2489;;;;;;J;\n" +
            "2016;06.10.2016;333452;3908;Horchheim;HORCHHEI;6;165;0;2016;;;A;A;7;3;05;05;730505;Rheinhessen;Bereich Wonnegau;Liebfrauenmorgen;Goldberg;;;;F;N;N;2016;;A;2;gerodet, nicht bestockt;;;;;;;;;01.11.2013;988;988;;;;;;J;\n" +
            "2016;06.10.2016;333452;3908;Horchheim;HORCHHEI;6;178;0;2016;;;A;A;7;3;05;05;730505;Rheinhessen;Bereich Wonnegau;Liebfrauenmorgen;Goldberg;;;;F;N;N;2016;;A;1;bestockt;124;Merlot;rot;K;124;Merlot;908;SO 4;01.04.2013;3914;3914;;;;;;J;\n" +
            "2016;06.10.2016;333452;3912;Weinsheim;WEINSHEZ;4;42;0;2016;;;A;A;7;3;05;04;730504;Rheinhessen;Bereich Wonnegau;Liebfrauenmorgen;Burgweg;;;;F;N;N;2016;;A;1;bestockt;053;Riesling;weiß;K;053;Riesling;908;SO 4;01.04.2008;7767;7767;;;;;;J;\n" +
            "2016;06.10.2016;333452;3912;Weinsheim;WEINSHEZ;6;28;0;2016;;;A;A;7;3;05;04;730504;Rheinhessen;Bereich Wonnegau;Liebfrauenmorgen;Burgweg;;;;F;N;N;2016;;A;1;bestockt;035;Müller-Thurgau;weiß;K;035;Müller-Thurgau;908;SO 4;01.04.2010;6371;6371;;;;;;J;\n" +
            "2016;06.10.2016;333452;3912;Weinsheim;WEINSHEZ;6;29;0;2016;;;A;A;7;3;05;04;730504;Rheinhessen;Bereich Wonnegau;Liebfrauenmorgen;Burgweg;;;;F;N;N;2016;;A;1;bestockt;035;Müller-Thurgau;weiß;K;035;Müller-Thurgau;908;SO 4;01.04.2009;7400;8402;;;;;;J;\n" +
            "2016;06.10.2016;333452;3912;Weinsheim;WEINSHEZ;6;29;0;2016;;;A;A;7;3;05;04;730504;Rheinhessen;Bereich Wonnegau;Liebfrauenmorgen;Burgweg;;;;F;N;N;2016;;A;1;bestockt;035;Müller-Thurgau;weiß;K;035;Müller-Thurgau;908;SO 4;01.04.2010;1002;8402;;;;;;J;\n" +
            "2016;06.10.2016;333452;3912;Weinsheim;WEINSHEZ;6;30;0;2016;;;A;A;7;3;05;04;730504;Rheinhessen;Bereich Wonnegau;Liebfrauenmorgen;Burgweg;;;;F;N;N;2016;;A;1;bestockt;035;Müller-Thurgau;weiß;K;035;Müller-Thurgau;908;SO 4;01.04.2009;3863;3863;;;;;;J;\n" +
            "2016;06.10.2016;333452;3912;Weinsheim;WEINSHEZ;6;68;2;2016;;;A;A;7;3;05;04;730504;Rheinhessen;Bereich Wonnegau;Liebfrauenmorgen;Burgweg;;;;F;N;N;2016;;A;1;bestockt;118;Spätburgunder;rot;K;118;Spätburgunder;902;125 AA;01.04.2001;2580;2580;;;;;;J;\n" +
            "2016;06.10.2016;333452;3907;Wiesoppenheim;WIESOPPE;2;178;0;2016;;;A;A;7;3;05;02;730502;Rheinhessen;Bereich Wonnegau;Liebfrauenmorgen;Am Heiligen Häuschen;;;;F;N;N;2016;;A;2;gerodet, nicht bestockt;;;;;;;;;01.04.2016;2038;2038;;;;;;J;\n";

    @Test
    public void parse() throws Exception {

        WipLwkRlpParser parser = (WipLwkRlpParser) ParserFactory.createParser(ParserVersion.WIP_LWK_RLP, INPUT);
        DataSet dataSet = parser.parse();

        assertEquals(dataSet.getAcres().size(), 6);
        assertEquals(dataSet.getCompanies().size(), 1);
        assertEquals(dataSet.getRegions().size(), 1);
        assertEquals(dataSet.getZones().size(), 1);
        assertEquals(dataSet.getGrossLagen().size(), 1);
        assertEquals(dataSet.getEinzelLage().size(), 3);
    }

}