package edu.uncc.wins.gestureslive;

import android.content.res.AssetManager;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by jbandy3 on 6/29/2015.
 */
public class Constants {
    public static final boolean SHOW_DIALOGS = false;
    public static final boolean VIBRATE_FOR_SEGMENT = true;
    public static final boolean VIBRATE_FOR_GESTURE = false;

    public static final int COORDINATE_CACHE_SIZE = 256;


    public static final double[][] MODEL_SINGLE_POINT;
    static {
        MODEL_SINGLE_POINT = txtTo2DArray("LogRegModel.txt");
    }

/*
    public static final double[][] MODEL_SINGLE_POINT = {

//            1. Celebratory Fist Pump
//            2. High Wave
//            3. Hand Shake
//            4. Fist Bump
//            5. Low Wave
//            6. Point
//            7. Motion Over
//            8. High Five
//            9. Applause/Clap

            {-0.2290525,0.97083804,0.11991338,0.98025067,0.04264776,-0.04114591,2.15193028,0.35774908,-1.64578298,0.87044627,-0.20938853,-1.23662649,2.02001965,-1.71910513,-0.41485726,-34.0598684,0.,-13.30992055,3.72642115,-0.10527761,0.09559909,-0.01223023,0.11251629,0.00197206,-0.29130338,-1.34390105,-0.84219531,-3.70930686,-8.93824498,-8.82752421,0.01375059,0.00512756,-0.02945139,0.0116799,0.00463818,-0.01135656,-0.03107152,0.02095998,0.07111233,0.01687299,-0.0120072,0.00510851,-0.00347153,0.01211053,0.0036966,0.00708099,-0.06293752,0.01677317,0.01349599,-0.01529285,0.08243593,0.04323816,-0.02468052,-0.03086768,0.05063492,0.05743087,-0.08290345,-0.01622769,-0.14492523,-0.02277652},
            {0.15631586,-0.09901359,0.77288315,-1.61395343,-0.02059023,0.19307921,0.02597361,-3.56513533,-3.02103721,-0.15996391,0.16092074,-1.44666162,1.18064077,-3.25007836,0.42771635,-11.436591,0.2712715,4.29995453,-0.84084683,-1.15678367,-0.5388325,0.01929445,-0.09031883,-0.07280908,-0.39809863,-0.77584818,-1.87956132,-19.62435676,2.63025399,8.23626697,-0.00163757,-0.02084513,-0.03398032,0.00735766,0.00482725,-0.01070054,0.0058675,-0.00137195,0.01080867,-0.01784295,-0.05220092,0.0234299,0.08262147,0.04263103,-0.02990984,0.08986935,0.07008008,0.03904521,0.03989944,-0.00929631,-0.02339363,0.02376336,-0.0265254,0.02719128,-0.02042785,0.02018051,0.03412583,0.0527472,0.02611368,-0.06557722},
            {-0.15624313,-0.79802422,-0.42298589,-0.35187808,0.38626999,-0.002325,-1.62261251,0.48797365,-0.23850493,-1.25406253,0.94769889,-3.08472871,0.25551149,0.19360223,2.16186249,0.,3.64422746,5.6424065,0.91071797,0.03366587,-0.0464523,0.03985953,-0.16248024,-0.02228394,-0.81186082,-0.03923121,-0.49384065,10.37343299,-2.45930528,3.62785454,-0.01276299,-0.0013903,-0.01010523,0.00434973,0.01849558,-0.0064601,0.00825468,-0.03649104,-0.08005382,-0.06945785,-0.07261959,-0.01705944,-0.04005992,0.00364017,0.00212815,-0.04899613,-0.12519901,-0.07917272,-0.05226006,-0.02343105,0.02181548,0.06664689,-0.01527776,-0.06765689,-0.04705123,-0.02260309,-0.01851797,0.01443496,0.01140824,-0.01638762},
            {-0.77438042,-0.17487567,-0.09907855,1.27268736,0.21605723,0.60407965,-1.94942555,0.79072284,1.27825968,-1.98098264,-3.651699,-2.51810264,-0.8747037,2.88620322,0.54720051,0.05318842,45.52173016,-11.00004608,0.03482651,0.4981067,-0.70171481,-0.01368619,0.22729494,0.05309763,0.23788225,1.76262867,0.8495237,0.,-2.0875114,-3.66376963,-0.01664777,0.0057843,0.04176294,-0.01179635,-0.02234333,-0.05787128,0.01037583,-0.0805384,-0.05985273,-0.02129156,0.05617608,0.03862635,0.02931673,0.03551699,0.00448358,-0.080182,0.01561648,-0.01655538,-0.0438529,0.02855142,0.00202842,-0.03851192,-0.05603988,0.07253385,0.03553241,-0.02442424,0.0201705,-0.0419954,0.01080736,-0.08500244},
            {-0.36371603,-0.00358272,-0.02105963,0.15832491,2.69261349,0.71230847,0.,-0.16837899,0.,-0.14657473,6.72177959,0.00624582,2.38366265,5.06616562,-6.03394,-33.03989223,7.85328934,0.37510393,1.48705002,-3.32450532,-0.02611717,-0.32871864,-0.7377282,0.02433126,0.09891829,-1.59755849,0.28952617,-32.78959234,15.15399275,2.89028053,0.0101301,-0.00653076,0.05997323,-0.02705457,-0.03127467,-0.04037229,-0.01961943,-0.00263452,0.05470546,-0.00467462,0.04474014,-0.07233744,-0.02855698,-0.08333362,-0.03107986,-0.11875411,-0.07523347,-0.04830967,-0.02990853,0.04671878,0.02460802,0.02323621,-0.0749427,0.0017734,0.0201444,0.0876237,0.01522805,-0.02772069,0.01017153,0.12545277},
            {-0.31984674,2.10210722,-0.198844,-1.79255015,0.69108552,0.1110353,0.54553769,1.87823431,1.10321129,-0.69734705,-0.88456172,-1.80667851,-1.93805096,0.2844744,1.0988689,-14.91077099,-9.44773497,-5.31214704,-1.41538584,-0.82714357,-0.43135667,-0.40330598,-0.01179222,-0.01455168,0.18635543,-0.89028344,-1.95926295,5.05653293,-14.17825197,-6.13416118,0.00553493,0.01928454,0.01734829,-0.00313771,0.00310389,-0.01896272,0.01175874,-0.01018937,-0.00673002,0.02762313,0.02359525,0.05701744,0.09816343,0.06258542,0.01836221,0.04428827,0.00640326,0.02575045,-0.03252166,-0.0130416,0.00611707,-0.1059684,-0.01798717,-0.01015199,0.06657453,0.0663504,0.01308936,-0.02983697,-0.05302901,-0.1012349},
            {-0.37117411,-0.17092524,-0.42879049,0.08475912,-0.44258449,0.03842903,0.,1.09081034,-4.11413983,-0.17337928,-4.23676366,-0.50965924,0.17891001,-0.5466775,1.16048509,0.,0.,15.06386989,0.62750472,0.47101454,0.16174081,-0.8640121,-0.16837213,-0.09055102,-0.39720498,0.23526839,0.09145569,-6.19607995,0.,-6.44285313,-0.00291078,0.0085143,-0.05858386,-0.01442659,-0.01020393,-0.06942559,-0.02469525,-0.03390518,0.03863392,-0.01436076,0.04991497,-0.00389313,0.00655727,-0.02627515,0.00839781,-0.04567824,-0.02594543,-0.07549547,-0.04200488,0.00331304,-0.08177115,-0.07828653,-0.02272171,-0.04226824,-0.05066208,0.1189336,0.1584781,0.18212198,0.17239099,0.1728635},
            {-0.42306179,-1.5591099,-0.49687398,1.36574312,0.14023498,0.84271696,0.07060351,-3.53573473,0.,-0.49179255,-0.87278332,-0.40391485,-3.89147104,-1.14819333,0.92253859,3.28367425,9.76176764,-4.85553953,0.65081551,0.49506137,0.29197406,-0.5568952,-0.21773193,0.0316373,-0.31329464,0.0110029,-0.75156026,0.,-11.5159265,-8.11346451,-0.00064139,-0.00075642,-0.01879047,0.00079785,-0.00836665,-0.00624752,-0.0121361,-0.0039728,-0.06748559,0.0390042,0.06183862,0.00778538,-0.00107876,-0.01201553,-0.00452955,0.02760265,0.02542523,-0.08341947,0.00305535,-0.0884747,-0.04788486,-0.06225287,-0.07735847,-0.05722844,0.07047055,-0.02224719,0.03657192,0.02410224,-0.00342572,-0.05143111},
            {-0.07764603,0.95217496,0.72757861,-1.90424901,-0.47445405,0.71311952,0.71173243,0.03011732,-0.37749389,-0.03065505,-2.42499631,2.39861391,-0.01188931,-1.41960296,-0.68836229,26.38911732,15.95152098,12.04733984,0.21256619,0.17836787,1.61864459,-0.08372622,0.01244685,-0.16561827,-0.4610053,-0.8646239,-1.59517922,0.,3.05412538,14.00814394,-0.00633261,-0.00041264,0.00144648,-0.00008645,-0.00826649,0.04074263,-0.01368981,-0.00440537,-0.00198715,-0.05827458,-0.05172523,-0.07957521,-0.07564945,0.07354637,0.00020207,0.01314127,-0.09342078,0.02918428,0.00252117,0.00035863,0.02229949,-0.11342964,-0.01066395,0.03935729,-0.06019072,-0.0291646,-0.03742845,-0.03606592,0.00174141,-0.0043746}

    };
*/

    public static final Map<Integer, String> SINGLE_POINT_INDECES_MAP;

    static {
        SINGLE_POINT_INDECES_MAP = new HashMap<Integer, String>();
        SINGLE_POINT_INDECES_MAP.put(0, "FIST PUMP");
        SINGLE_POINT_INDECES_MAP.put(1, "HIGH WAVE");
        SINGLE_POINT_INDECES_MAP.put(2, "HAND SHAKE");
        SINGLE_POINT_INDECES_MAP.put(3, "FIST BUMP");
        SINGLE_POINT_INDECES_MAP.put(4, "LOW WAVE");
        SINGLE_POINT_INDECES_MAP.put(5, "POINT");
        SINGLE_POINT_INDECES_MAP.put(6, "MOTION OVER");
        SINGLE_POINT_INDECES_MAP.put(7, "HIGH FIVE");
        SINGLE_POINT_INDECES_MAP.put(8, "APPLAUSE");
    }

    public static final double[][] MODEL = {

            /* THIS IS NOT FOR SURE, BASED ON GUESSING ORDER, should be mostly right though
            1. Celebratory Fist Pump
            2. High Wave
            3. Hand Shake
            4. Fist Bump
            5. Low Wave
            6. Point S
            7. Point L
            8. Point R
            9. Point U
            10. Motion Over
            11. High Five
            12. Applause/Clap
             */
            {-0.29235374,0.73379212,0.19981378,0.76963291,0.04168262,0.11832439,2.27142843,0.80740578,-3.64919125,0.75367795,-0.48244972,-3.45188328,2.09483512,-1.79172638,-0.65598216,-24.77716485,0.,-11.58720495,4.33902328,0.00322118,0.04568652,0.003264,0.09661648,0.00942498,-0.40888497,-1.4724232,-0.34315317,-8.11993481,-8.25576557,-9.3273321,0.01625292,-0.00409279,-0.01984996,0.00987217,0.0050814,-0.00281958,-0.0367414,0.030098,0.07417317,0.02331925,-0.02274901,0.00003827,-0.0053467,0.00955264,0.00599384,-0.00261241,-0.06113213,0.01418484,0.01245518,-0.01155487,0.08504759,0.04926971,-0.02888813,-0.02506348,0.0454901,0.05560699,-0.07747764,-0.02320431,-0.14233325,-0.00820461},
            {0.14899679,-0.39610858,0.90623241,-2.68357911,0.35695567,0.53162028,0.14127367,-3.48373878,-2.72533146,-0.00020537,-0.21007236,-0.84432337,1.53517128,-3.4977607,0.,-6.82599161,0.,0.07237422,0.30947947,-1.2455696,-0.81887174,0.16143152,-0.22604307,-0.05303018,-0.96337025,-1.76712766,-1.90286452,-16.7381656,11.79953181,13.06892449,0.01437355,-0.03701052,-0.03079646,0.01435698,0.00573032,-0.04447873,0.00551777,0.01814781,0.01210558,-0.02271829,-0.08824717,0.02822436,0.0717681,0.08566886,-0.03226329,0.13175932,0.06378634,0.03043767,0.0597562,-0.02455761,-0.04081393,0.03065676,-0.01257598,0.04423122,0.02060066,0.0323508,0.04841642,0.0818395,0.05046559,-0.05227904},
            {-0.08014641,-0.83613567,0.37713194,-0.22964296,0.23369998,-0.20464493,-1.58500625,0.05685869,-0.45107912,-0.18434432,2.30177769,-0.6256563,-0.07249457,0.11879353,1.52959278,0.,0.32637602,5.68754092,0.97507112,0.08924997,-0.07493555,0.04012173,-0.10119315,0.00273443,-0.55379255,0.1726305,-1.46863231,17.45584895,-4.61581267,0.75371436,-0.01537354,-0.00063966,-0.02076139,0.00192886,0.01108834,-0.003774,-0.0010614,-0.03285087,-0.07711091,-0.05644797,-0.06188968,-0.08137545,-0.01863309,-0.02370984,0.00369868,-0.03964106,-0.10912119,-0.07927226,-0.05023095,-0.03279448,0.02413742,0.06361412,-0.01998485,-0.05000713,-0.04486537,-0.05909764,0.01580133,0.0050261,0.0169656,0.00476606},
            {-0.79369149,-0.32130044,-0.27321981,1.3192382,0.30899408,0.48008146,-1.58150329,0.01166396,1.35102385,-2.64362758,-2.95859737,-2.47567568,-1.38042559,2.83209569,0.6109452,10.27787904,44.80841892,-14.26966714,0.12220544,0.34935379,-0.50949913,-0.02976981,0.18381657,0.05261215,0.05925015,1.81875412,0.15032905,0.,1.42880172,-0.3704163,-0.01183742,0.00300148,0.02455042,-0.01576729,-0.02395955,-0.05695829,0.01074978,-0.07240586,-0.04091057,-0.01465084,0.04747038,0.01074492,0.03586194,0.05851819,0.00094778,-0.08190945,0.01343477,-0.03659054,-0.0276419,0.0506343,-0.01719719,-0.04697437,-0.05497125,0.0712047,0.0203245,-0.03099496,0.0143415,-0.05455382,0.00686034,-0.02724996},
            {-0.11550259,-0.36784386,-0.06594271,-0.02597185,1.18212972,0.02231929,1.18953981,-0.97917394,2.36861022,-0.55333368,1.22474583,0.4727875,1.24566839,3.27172668,-4.57095794,-25.73682568,13.3454322,5.83563897,1.22431394,-2.34583344,0.42834185,-0.13841882,-0.40288725,0.01545607,-0.06070863,-0.48780073,0.30047688,-38.07799155,10.86095955,2.72677191,0.00612994,-0.00824765,0.03160533,-0.00748252,-0.01024412,-0.00931884,-0.01286094,-0.00632651,0.02905487,-0.01543052,0.01962186,-0.04070052,-0.01422742,-0.07677583,-0.01065724,-0.07942073,-0.05883035,-0.03652165,0.00438543,0.0487829,0.0058826,0.05143545,-0.05531434,-0.01532074,0.0033049,0.04758449,-0.00307502,-0.00978973,0.01042648,0.07160701},
            {-0.27654285,0.31049356,0.16196973,-0.78267262,0.32144434,0.34750123,0.07963594,1.26728754,1.21496168,-0.37475128,-0.56589812,-0.88645539,-2.393171,2.09236087,1.1686349,3.25651318,-13.463851,5.02470987,0.12562473,-0.18053114,-0.32226664,-0.17428915,-0.05754037,0.03145981,-1.38043351,-0.75536325,-0.95306753,0.89190482,-7.81781527,2.4114443,-0.00125558,0.00638032,0.0149154,-0.01249311,-0.0008179,-0.02646006,-0.01084907,-0.01022607,-0.03483854,0.04251376,0.05889875,0.08406372,0.05429095,-0.03641685,0.00701221,-0.10990965,0.02707073,0.05414836,0.04137255,0.00795518,0.00126346,-0.07453547,-0.02019113,0.01995831,-0.10895674,0.03411804,-0.01720465,0.01537809,0.00803775,-0.02255453},
            {0.23381097,0.63114832,0.62033406,-0.71714791,0.24334723,0.07785893,-0.62000693,1.21634391,-0.67395011,-1.2575375,-0.48530949,0.37100723,-0.38564605,1.27502972,-0.70517129,-2.24877983,-3.14004068,7.12564678,-0.10088649,-0.25097267,-0.45183732,0.10865743,-0.08818575,0.02416543,-0.66638906,-0.12191812,1.58768513,-5.09192037,-12.5160974,-9.04358024,-0.00650021,0.00751675,-0.00996522,-0.01477993,0.00341084,0.0056028,-0.00694631,-0.04285882,-0.03662147,-0.02375438,0.04191849,-0.01012845,-0.02302732,0.10048912,0.00968416,0.04461157,-0.00656051,0.03017215,-0.01846108,0.00485409,0.01037497,-0.01823513,0.01029527,0.02955471,0.06284259,0.04480211,-0.00875112,-0.04064729,-0.04493104,-0.08108147},
            {-0.16869992,1.78137756,-2.43779672,-0.395638,0.55595487,1.55100323,-5.03811203,-1.26680602,1.65572399,0.2883489,-4.07989975,-6.06349481,2.36922172,-3.87104713,-1.48731601,0.,-30.15546208,0.,-3.00521492,-1.04973066,0.11992839,-0.56567752,0.12243645,-0.12011356,2.92717531,-3.29247224,-3.33594263,-8.32619512,0.12327014,-6.40389377,-0.03302981,-0.00701163,0.0277664,-0.04717277,-0.02493583,-0.05758135,0.01123069,-0.03981976,-0.00037082,0.0560695,0.00363261,0.08164405,0.06222879,0.06118868,-0.0062159,-0.01857295,0.01944561,0.03648113,-0.00563432,-0.01311925,0.03997917,0.07323184,0.01440342,-0.02724958,0.11522635,0.08996489,0.0649467,-0.00174128,-0.05589587,-0.06250575},
            {-0.70951787,0.36150782,0.33366268,-1.86454169,0.17498164,0.65075394,3.45815144,0.4068158,-0.60315422,0.37208052,-0.01010964,-1.0157634,-3.76350708,2.25326887,2.72791365,-16.01325179,0.,-6.08902653,1.30248693,0.80543023,-0.09098057,-0.30141193,-0.1430288,-0.08388239,0.97941105,0.19390556,-3.89229748,-4.75728008,-11.63821132,-5.23844349,0.01165207,0.00367569,-0.02179791,0.01791823,-0.0012511,-0.0231179,-0.01181746,-0.00180244,0.03587348,-0.02658493,-0.00001914,-0.0220629,0.05029772,0.01860653,0.00589363,0.04676124,0.01981942,-0.01742068,-0.00892286,-0.01068812,-0.02998052,-0.02518096,-0.03876944,-0.03439511,0.04394669,0.0230415,0.02012509,0.02439216,-0.0598756,-0.06752071},
            {-0.3588798,0.16204738,-0.29400231,0.12527318,-0.16785875,0.09393659,1.47639271,0.1028527,-5.14526061,-0.298112,-3.05272986,-0.07644046,-0.03700042,-0.94832029,0.44679397,0.,0.,10.37139384,1.07541196,0.33344458,0.22637427,-0.82451568,-0.18279383,-0.06648693,-0.27852424,-0.21178347,0.71665584,-8.58593399,0.56659057,-5.11902439,-0.00400963,0.00285668,-0.04760435,-0.01406127,-0.00894449,-0.05049979,-0.01451852,-0.02035004,0.03524757,-0.00506491,0.04977542,0.00286167,0.02235016,-0.04261349,0.00354716,-0.05333391,-0.04169376,-0.07516059,-0.04908749,0.01074577,-0.05599044,-0.06286486,-0.03156414,-0.05319918,-0.06497339,0.07292384,0.14108804,0.16330879,0.14786582,0.14374136},
            {-0.47241774,-1.78109824,-0.53091513,1.39937082,0.09224798,0.73932614,-0.35685394,-1.9163714,-1.26065803,-0.35218586,-1.38943978,0.01889077,-3.36604743,-1.11739937,0.96284954,11.41473347,12.66422676,-2.41093961,0.73771943,0.41794271,0.36739754,-0.50783306,-0.20220359,0.03302884,-0.38323832,-0.14861098,0.,0.,-11.74182581,-7.50105983,-0.00033437,-0.01056733,-0.01525324,0.00023086,-0.01113017,-0.01258425,-0.01095572,-0.00975423,-0.06986632,0.04432955,0.04023363,0.01866697,-0.01873792,-0.00728146,-0.01703036,0.01261197,0.02782477,-0.06856926,0.00982087,-0.07780667,-0.03486529,-0.05841886,-0.06559427,-0.05774205,0.04628162,-0.02579347,0.03991698,0.01756507,-0.00497692,-0.04103957},
            {-0.2406996,1.16660424,0.61163954,-2.61704341,-0.36772792,1.29100008,-0.0362966,0.22692143,-0.6577866,0.50919137,-2.8358377,3.48170352,-0.11917055,-1.14746384,-1.15668772,28.50595056,23.84482752,7.13737251,0.47595546,0.14481286,2.32838452,-0.03999252,0.04976984,-0.33485181,0.01913385,-0.46367069,-3.24488686,-0.80625342,1.32923709,18.05502746,0.00204314,0.00523942,-0.01288912,0.00362361,-0.01152189,0.0457372,-0.0190108,0.00567279,0.00994671,-0.04553673,-0.08685343,-0.10548531,-0.12091427,0.13374522,-0.00383552,0.0283589,-0.11592567,0.01915114,0.00216783,0.02243227,0.02792117,-0.12847227,-0.01731511,0.03810546,-0.11852609,-0.05876423,-0.04665076,-0.05796638,0.02539006,-0.02894757}
    };


    public static final int[] trial0StartPoints = {
            2069,
            2304,
            2538,
            3174,
            3564,
            4047,
            4553,
            4840,
            5067,
            5482,
            5687,
            5875,
            6262,
            6512,
            6783,
            7215,
            7464,
            7694,
            8072,
            8368,
            8650,
            9014,
            9201,
            9450,
            9782,
            10096,
            10414,
            10800,
            11114,
            11454,
            11895,
            12156,
            12402,
            12739,
            13098,
            13457,
            14010,
            14394,
            14862,
            15718,
            15982,
            16276,
            16688,
            17074,
            17478,
            17918,
            18196,
            18499,
            18991,
            19344,
            19577,
            19955,
            20267,
            20691,
            21170,
            21461,
            21766,
            22161,
            22385,
            22669,
            23142,
            23432,
            23785,
            24356,
            24665,
            24957,
            25422,
            25768,
            26132,
            26536,
            26807,
            27096,
            27623,
            28035,
            28437,
            28970,
            29309,
            29747,
            30793,
            31110,
            31424,
            31960,
            32371,
            32806,
            33386,
            33698,
            34040,
            34514,
            34779,
            35040,
            35484,
            35827,
            36244,
            36671,
            36972,
            37276,
            37735,
            38030,
            38364,
            38756,
            39094,
            39452,
            39977,
            40337,
            40666,
            41086,
            41492,
            41926,
            42356,
            42635,
            42913,
            43329,
            43770,
            44165,
            44812,
            45290,
            45791,
            46717,
            47030,
            47356,
            47870,
            48302,
            48753,
            49421,
            49886,
            50200,
            50782,
            51071,
            51352,
            51864,
            52282,
            52713,
            53262,
            53571,
            53916,
            54329,
            54666,
            55017,
            55504,
            55807,
            56141,
            56571,
            56879,
            57234,
            57771,
            58168,
            58588,
            59103,
            59378,
            59684,
            60177,
            60595,
            60977,
            61572,
            61915,
            62341,
            63352,
            63692,
            63991,
            64559,
            64954,
            65385,
            65946,
            66247,
            66526,
            66986,
            67252,
            67512,
            68019,
            68435,
            68849,
            69438,
            69761,
            70136,
            70728,
            71063,
            71417,
            71998,
            72309,
            72594,
            73143,
            73468,
            73806,
            74372,
            74806,
            75226,
            75774,
            76066,
            76364,
            76905,
            77347,
            77747,
            78416,
            78852,
            79371};


    public static double[] txtTo1DArray(String fileName) {
        AssetManager mng = MainActivity.appAssets;
        InputStream strm = null;
        try {
            strm = mng.open(fileName);
        } catch (IOException e) {
            Log.v("TAG", "IOException :D");
        }

        Scanner scanner = new Scanner(strm);
        ArrayList<Double> doublesArrayList = new ArrayList<Double>();

        int linecount = 0;
        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            linecount++;

            String[] columns = line.split("\\s+");
            for (int i=0; i<columns.length; i++) {
                if (!columns[i].contains("[") && !columns[i].contains("]") && (columns[i].trim().length() > 0)){
                    doublesArrayList.add(Double.parseDouble(columns[i]));
                }
            }
        }

        double[] toReturn = new double[doublesArrayList.size()];
        for(int i = 0; i < toReturn.length; i++)
            toReturn[i] = doublesArrayList.get(i);
        return toReturn;
    }



    public static double[][] txtTo2DArray(String fileName){
        AssetManager mng = MainActivity.appAssets;
        InputStream strm = null;
        try {
            strm = mng.open(fileName);
        } catch (IOException e) {
            Log.v("TAG", "IOException :D");
        }

        Scanner scanner = new Scanner(strm);

        ArrayList<ArrayList<Double>> doublesArr = new ArrayList<ArrayList<Double>>();

        int linecount = 0;
        int arraySizeCount = 0;
        while(scanner.hasNextLine()){
            if(arraySizeCount == 0) {
                doublesArr.add(linecount, new ArrayList<Double>());
            }

            String line = scanner.nextLine();
            String[] columns = line.split("\\s+");

            for (int i=0; i<columns.length; i++) {
                if(columns[i].contains("]")){
                    linecount++;
                    arraySizeCount = 0;
                    i = columns.length;
                }
                else if (!columns[i].contains("[") && (columns[i].trim().length() > 0)){
                    doublesArr.get(linecount).add(arraySizeCount++,Double.parseDouble(columns[i]));
                }
            }
        }

        double[][] toReturn = new double[linecount][doublesArr.get(0).size()];
        Log.v("TAG","dimensions (expect 9 x 60): " + linecount + " x " + doublesArr.get(0).size());

        for(int c = 0; c < linecount; c++){
            for(int j = 0; j < toReturn[0].length; j++)
                toReturn[c][j] = doublesArr.get(c).get(j);
            Log.v("TAG","Array " + c + ": " + Arrays.toString(toReturn[c]));

        }

        return toReturn;
    }

}
