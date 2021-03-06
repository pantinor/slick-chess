package chess.media;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.log4j.Logger;
import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.ImageBuffer;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.util.BufferedImageUtil;

public class BamAnimationStore {

    public static final int DURATION = 125;

    public LinkedHashMap<String, Animation> animations = new LinkedHashMap<String, Animation>(60);

    private static final Logger logger = Logger.getLogger(BamAnimationStore.class);

    byte transparent;
    Palette palette;
    String sheetName;
    int numSeqs = 0;
    int maxFramesInSeq = 0;
    int maxWidth;
    int maxHeight;

    public static final int MIN_FRAMES_PER_ANIM = 5;
    public static final int MAX_SEQUENCES = 150;

    boolean crop = false;

    public static final String ATTACK1 = "ATTACK1";
    public static final String ATTACK2 = "ATTACK2";
    public static final String ATTACK3 = "ATTACK3";
    public static final String ATTACK4 = "ATTACK4";
    public static final String CASTING1 = "CASTING1";
    public static final String CASTING2 = "CASTING2";
    public static final String DIE = "DIE";
    public static final String GETUP = "GETUP";
    public static final String HIT1 = "HIT1";
    public static final String HIT2 = "HIT2";
    public static final String RANGEDATTACK1 = "RANGEDATTACK1";
    public static final String RANGEDATTACK2 = "RANGEDATTACK2";
    public static final String RUN = "RUN";
    public static final String SLEEP = "SLEEP";
    public static final String SPELL = "SPELL";
    public static final String STAND1 = "STAND1";
    public static final String STAND2 = "STAND2";
    public static final String STAND3 = "STAND3";
    public static final String TALK1 = "TALK1";
    public static final String TALK2 = "TALK2";
    public static final String TWITCH = "TWITCH";
    public static final String WALK = "WALK";

    public static final String ATTACK1_EAST = "ATTACK1-EAST";
    public static final String ATTACK2_EAST = "ATTACK2-EAST";
    public static final String ATTACK3_EAST = "ATTACK3-EAST";
    public static final String ATTACK4_EAST = "ATTACK4-EAST";
    public static final String CASTING1_EAST = "CASTING1-EAST";
    public static final String CASTING2_EAST = "CASTING2-EAST";
    public static final String DIE_EAST = "DIE-EAST";
    public static final String GETUP_EAST = "GETUP-EAST";
    public static final String HIT1_EAST = "HIT1-EAST";
    public static final String HIT2_EAST = "HIT2-EAST";
    public static final String RANGEDATTACK1_EAST = "RANGEDATTACK1-EAST";
    public static final String RANGEDATTACK2_EAST = "RANGEDATTACK2-EAST";
    public static final String RUN_EAST = "RUN-EAST";
    public static final String SLEEP_EAST = "SLEEP-EAST";
    public static final String SPELL_EAST = "SPELL-EAST";
    public static final String STAND1_EAST = "STAND1-EAST";
    public static final String STAND2_EAST = "STAND2-EAST";
    public static final String STAND3_EAST = "STAND3-EAST";
    public static final String TALK1_EAST = "TALK1-EAST";
    public static final String TALK2_EAST = "TALK2-EAST";
    public static final String TWITCH_EAST = "TWITCH-EAST";
    public static final String WALK_EAST = "WALK-EAST";

    public static ArrayList<String> animationTypes = new ArrayList<String>();

    static {
        animationTypes.add(ATTACK1);
        animationTypes.add(ATTACK2);
        animationTypes.add(ATTACK3);
        animationTypes.add(ATTACK4);
        animationTypes.add(CASTING1);
        animationTypes.add(CASTING2);
        animationTypes.add(DIE);
        animationTypes.add(GETUP);
        animationTypes.add(HIT1);
        animationTypes.add(HIT2);
        animationTypes.add(RANGEDATTACK1);
        animationTypes.add(RANGEDATTACK2);
        animationTypes.add(RUN);
        animationTypes.add(SLEEP);
        animationTypes.add(SPELL);
        animationTypes.add(STAND1);
        animationTypes.add(STAND2);
        animationTypes.add(STAND3);
        animationTypes.add(TALK1);
        animationTypes.add(TALK2);
        animationTypes.add(TWITCH);
        animationTypes.add(WALK);
        animationTypes.add(ATTACK1_EAST);
        animationTypes.add(ATTACK2_EAST);
        animationTypes.add(ATTACK3_EAST);
        animationTypes.add(ATTACK4_EAST);
        animationTypes.add(CASTING1_EAST);
        animationTypes.add(CASTING2_EAST);
        animationTypes.add(DIE_EAST);
        animationTypes.add(GETUP_EAST);
        animationTypes.add(HIT1_EAST);
        animationTypes.add(HIT2_EAST);
        animationTypes.add(RANGEDATTACK1_EAST);
        animationTypes.add(RANGEDATTACK2_EAST);
        animationTypes.add(RUN_EAST);
        animationTypes.add(SLEEP_EAST);
        animationTypes.add(SPELL_EAST);
        animationTypes.add(STAND1_EAST);
        animationTypes.add(STAND2_EAST);
        animationTypes.add(STAND3_EAST);
        animationTypes.add(TALK1_EAST);
        animationTypes.add(TALK2_EAST);
        animationTypes.add(TWITCH_EAST);
        animationTypes.add(WALK_EAST);
    }

    public BamAnimationStore(String bamDir, String filePrefix) {

        this.sheetName = filePrefix;

        Collection<File> infiles = getFiles(bamDir, filePrefix + "*");

        ArrayList<Bam> bams = new ArrayList<Bam>();

        boolean processEast = true;

        try {

            for (File inFile : infiles) {

                if (getAnimationName(inFile.getName(), "ANIMATION").equals("ANIMATION")) {
                    logger.debug("WARNING: could not find known animation name for " + inFile.getName() + " and skipping it.");
                    continue;
                }

                if (getAnimationName(inFile.getName(), "ANIMATION").contains("EAST")) {
                    processEast = false;
                }

                byte[] buffer = FileUtils.readFileToByteArray(inFile);

                String signature = new String(buffer, 0, 4);
                if (signature.equals("BAMC")) {
                    buffer = decompress(buffer);
                } else if (!signature.equals("BAM ")) {
                    throw new Exception("Unsupported BAM-file: " + signature);
                }

                int numberframes = convertShort(buffer, 8);
                int numberanims = convertUnsignedByte(buffer, 10);
                this.transparent = buffer[11];
                int frameOffset = convertInt(buffer, 12);
                int paletteOffset = convertInt(buffer, 16);
                int lookupOffset = convertInt(buffer, 20);

                this.palette = new Palette(buffer, paletteOffset, lookupOffset - paletteOffset);

                Frame[] frames = new Frame[numberframes];
                for (int i = 0; i < numberframes; i++) {
                    frames[i] = new Frame(buffer, frameOffset + 12 * i);
                }

                int animOffset = frameOffset + 12 * numberframes;
                int lookupCount = 0;
                Anim[] anims = new Anim[numberanims];
                for (int i = 0; i < numberanims; i++) {
                    anims[i] = new Anim(buffer, animOffset + 4 * i);
                    lookupCount = Math.max(lookupCount, anims[i].getMaxLookup());
                }

                int[] lookupTable = new int[lookupCount];
                for (int i = 0; i < lookupCount; i++) {
                    lookupTable[i] = convertShort(buffer, lookupOffset + i * 2);
                }

                Bam bam = new Bam(inFile.getName(), frames, anims, lookupTable);
                bams.add(bam);

                for (int i = 0; i < anims.length; i++) {
                    Anim anim = anims[i];

                    if (numSeqs > MAX_SEQUENCES) {
                        logger.debug("WARNING: MAX SEQUENCES exceeded and skipping them.");
                        continue;
                    }

                    if (anim.frameCount < MIN_FRAMES_PER_ANIM) {
                        continue;
                    }

                    if (bam.getFrameNr(i, 0) == -1 || bam.getFrame(bam.getFrameNr(i, 0)).getWidth() < 2
                            || bam.getFrameNr(i, 1) == -1 || bam.getFrame(bam.getFrameNr(i, 1)).getWidth() < 2) {
                        continue; //skip images less than than 1 pixel size
                    }
                    if (anim.frameCount > maxFramesInSeq) {
                        maxFramesInSeq = anim.frameCount;
                    }

                    numSeqs++;

                }

            }

            if (maxFramesInSeq < 1) {
                throw new Exception("No Frames found in BAMS for prefix: " + sheetName);
            }

            int seqnum = 0;
            for (int x = 0; x < bams.size(); x++) {
                Bam bam = bams.get(x);
                Anim[] anims = bam.anims;
                for (int i = 0; i < anims.length; i++) {
                    Anim anim = anims[i];

                    if (seqnum > MAX_SEQUENCES) {
                        break;
                    }

                    if (anim.frameCount < MIN_FRAMES_PER_ANIM) {
                        continue;
                    }

                    if (bam.getFrameNr(i, 0) == -1 || bam.getFrame(bam.getFrameNr(i, 0)).getWidth() < 2
                            || bam.getFrameNr(i, 1) == -1 || bam.getFrame(bam.getFrameNr(i, 1)).getWidth() < 2) {
                        continue;
                    }

                    int count = 1;
                    Animation animation = null;
                    String animationName = getAnimationName(bam.bamFileName, "ANIMATION");
                    while (true) {
                        String tmp = animationName + "-" + count;
                        animation = animations.get(tmp);
                        if (animation == null) {
                            animation = new Animation();
                            animations.put(tmp, animation);
                            break;
                        } else {
                            count++;
                        }
                    }

                    for (int j = 0; j < anim.frameCount; j++) {

                        Image fr = bam.getFrame(bam.getFrameNr(i, j));
                        int fw = fr.getWidth();
                        int fh = fr.getHeight();

                        if (fw > maxWidth) {
                            maxWidth = fw;
                        }
                        if (fh > maxHeight) {
                            maxHeight = fh;
                        }

                        if (fw < 2 || fh < 2) {
                            continue;
                        }

                        animation.addFrame(fr, DURATION);
                    }
                    seqnum++;
                }
            }

            animations = sortAnimations(animations);

            if (processEast) {
                processEasternDirections();
            } else {
                indexByDegrees();
            }

            logger.debug("animations size=" + animations.size());

            for (String key : animations.keySet()) {
                Animation anim = animations.get(key);
                //call update manually in order to centerize the drawing of the frames
                anim.setAutoUpdate(false);
                //logger.debug("animations key ="+key);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(GameContainer gc, String prefix, int delta) {

        for (String key : animations.keySet()) {
            if (!key.startsWith(prefix)) {
                continue;
            }
            Animation anim = animations.get(key);
            anim.update(delta);
        }
    }

    public Animation getAnimation(String key) {
        return animations.get(key);
    }

    public Animation getAnimation(String prefix, int degrees) {
        return animations.get(prefix + "-" + degrees);
    }

    private LinkedHashMap<String, Animation> sortAnimations(LinkedHashMap<String, Animation> input) {
        SortedSet<String> keys = new TreeSet<String>(input.keySet());
        LinkedHashMap<String, Animation> sortedMap = new LinkedHashMap<String, Animation>();
        for (String key : keys) {
            sortedMap.put(key, input.get(key));
        }
        return sortedMap;
    }

    public void indexByDegrees() {

        for (String type : animationTypes) {

            LinkedHashMap<String, Animation> anims = null;

            for (int i = 1; i < 6; i++) {

                Animation anim = animations.remove(type + "-" + i);

                if (anim == null) {
                    break;
                }
                if (anims == null) {
                    anims = new LinkedHashMap<String, Animation>();
                }

				//logger.debug("Removed: " + type + "-" + i);
                if (i == 1 && type.contains("EAST")) {
                    String key = type.replace("-EAST", "") + "-" + MapDirection.NORTHEAST.getDegrees();
                    anims.put(key, anim);
                    //logger.debug("Added: " + key);
                    continue;
                }
                if (i == 2 && type.contains("EAST")) {
                    String key = type.replace("-EAST", "") + "-" + MapDirection.EAST.getDegrees();
                    anims.put(key, anim);
                    //logger.debug("Added: " + key);
                    continue;
                }
                if (i == 3 && type.contains("EAST")) {
                    String key = type.replace("-EAST", "") + "-" + MapDirection.SOUTHEAST.getDegrees();
                    anims.put(key, anim);
                    //logger.debug("Added: " + key);
                    continue;
                }
                if (i == MapDirection.INT_DIR8_SOUTH) {
                    String key = type + "-" + MapDirection.SOUTH.getDegrees();
                    anims.put(key, anim);
                    //logger.debug("Added: " + key);
                    continue;
                }
                if (i == MapDirection.INT_DIR8_SOUTHWEST) {
                    String key = type + "-" + MapDirection.SOUTHWEST.getDegrees();
                    anims.put(key, anim);
                    //logger.debug("Added: " + key);
                    continue;
                }
                if (i == MapDirection.INT_DIR8_WEST) {
                    String key = type + "-" + MapDirection.WEST.getDegrees();
                    anims.put(key, anim);
                    //logger.debug("Added: " + key);
                    continue;
                }
                if (i == MapDirection.INT_DIR8_NORTHWEST) {
                    String key = type + "-" + MapDirection.NORTHWEST.getDegrees();
                    anims.put(key, anim);
                    //logger.debug("Added: " + key);
                    continue;
                }
                if (i == MapDirection.INT_DIR8_NORTH) {
                    String key = type + "-" + MapDirection.NORTH.getDegrees();
                    anims.put(key, anim);
                    //logger.debug("Added: " + key);
                    continue;
                }
            }

            if (anims == null) {
                continue;
            }

            //add anims to animations 
            for (String key : anims.keySet()) {
                animations.put(key, anims.get(key));
            }

        }

        animations = sortAnimations(animations);

    }

	//add eastern directions and also convert from 16 directions to only 8
    //we dont need 16 directions, it is too many, 8 is enough
    public void processEasternDirections() {

        for (String type : animationTypes) {

            if (type.contains("EAST")) {
                continue;
            }

            LinkedHashMap<String, Animation> anims = null;

            for (int i = 1; i < 50; i++) {

                Animation anim = animations.remove(type + "-" + i);

                if (anim == null) {
                    break;
                }
                if (anims == null) {
                    anims = new LinkedHashMap<String, Animation>();
                }

				//logger.debug("Removed: " + type + "-" + i);
                if (i == MapDirection.INT_DIR16_SOUTH) {
                    String key = type + "-" + MapDirection.SOUTH.getDegrees();
                    anims.put(key, anim);
                    //logger.debug("Added: " + key);
                }
                if (i == MapDirection.INT_DIR16_SOUTHWEST) {
                    String key = type + "-" + MapDirection.SOUTHWEST.getDegrees();
                    anims.put(key, anim);
                    //logger.debug("Added: " + key);
                }
                if (i == MapDirection.INT_DIR16_WEST) {
                    String key = type + "-" + MapDirection.WEST.getDegrees();
                    anims.put(key, anim);
                    //logger.debug("Added: " + key);
                }
                if (i == MapDirection.INT_DIR16_NORTHWEST) {
                    String key = type + "-" + MapDirection.NORTHWEST.getDegrees();
                    anims.put(key, anim);
                    //logger.debug("Added: " + key);
                }
                if (i == MapDirection.INT_DIR16_NORTH) {
                    String key = type + "-" + MapDirection.NORTH.getDegrees();
                    anims.put(key, anim);
                    //logger.debug("Added: " + key);
                }
            }

            if (anims == null) {
                continue;
            }

            Animation sw = anims.get(type + "-" + MapDirection.SOUTHWEST.getDegrees());
            if (sw != null) {
                Animation se = new Animation();
                for (int i = 0; i < sw.getFrameCount(); i++) {
                    Image image = sw.getImage(i);
                    Image flipped = image.getFlippedCopy(true, false);
                    se.addFrame(flipped, DURATION);
                }
                anims.put(type + "-" + MapDirection.SOUTHEAST.getDegrees(), se);
                //logger.debug("Added: " + type+"-"+MapDirection.SOUTHEAST.getDegrees());
            }

            Animation w = anims.get(type + "-" + MapDirection.WEST.getDegrees());
            if (w != null) {
                Animation e = new Animation();
                for (int i = 0; i < w.getFrameCount(); i++) {
                    Image image = w.getImage(i);
                    Image flipped = image.getFlippedCopy(true, false);
                    e.addFrame(flipped, DURATION);
                }
                anims.put(type + "-" + MapDirection.EAST.getDegrees(), e);
                //logger.debug("Added: " + type+"-"+MapDirection.EAST.getDegrees());
            }

            Animation nw = anims.get(type + "-" + MapDirection.NORTHWEST.getDegrees());
            if (nw != null) {
                Animation ne = new Animation();
                for (int i = 0; i < nw.getFrameCount(); i++) {
                    Image image = nw.getImage(i);
                    Image flipped = image.getFlippedCopy(true, false);
                    ne.addFrame(flipped, DURATION);
                }
                anims.put(type + "-" + MapDirection.NORTHEAST.getDegrees(), ne);
                //logger.debug("Added: " + type+"-"+MapDirection.NORTHEAST.getDegrees());
            }

            //add anims to animations 
            for (String key : anims.keySet()) {
                animations.put(key, anims.get(key));
            }

        }

        animations = sortAnimations(animations);

    }

    public enum MapDirection {

        NORTH(0), NORTHEAST(45), EAST(90), SOUTHEAST(135), SOUTH(180), SOUTHWEST(225), WEST(270), NORTHWEST(315);

        private int degrees;

        public static final int INT_DIR8_SOUTH = 1;
        public static final int INT_DIR8_SOUTHWEST = 2;
        public static final int INT_DIR8_WEST = 3;
        public static final int INT_DIR8_NORTHWEST = 4;
        public static final int INT_DIR8_NORTH = 5;
        public static final int INT_DIR8_NORTHEAST = 6;
        public static final int INT_DIR8_EAST = 7;
        public static final int INT_DIR8_SOUTHEAST = 8;

        public static final int INT_DIR16_SOUTH = 1;
        public static final int INT_DIR16_SOUTHWEST = 3;
        public static final int INT_DIR16_WEST = 5;
        public static final int INT_DIR16_NORTHWEST = 7;
        public static final int INT_DIR16_NORTH = 9;

        MapDirection(int degrees) {
            this.degrees = degrees;
        }

        public int getDegrees() {
            return degrees;
        }

        public void setDegrees(int degrees) {
            this.degrees = degrees;
        }

    }

    public static String getAnimationName(String fileName, String defaultName) {
        String animationName = defaultName;
        fileName = fileName.toUpperCase();

        if (fileName.endsWith("A1.BAM")) {
            animationName = ATTACK1;
        }
        if (fileName.endsWith("G2.BAM")) {
            animationName = ATTACK1;
        }
        if (fileName.endsWith("AT1.BAM")) {
            animationName = ATTACK1;
        }
        if (fileName.endsWith("A2.BAM")) {
            animationName = ATTACK2;
        }
        if (fileName.endsWith("G21.BAM")) {
            animationName = ATTACK2;
        }
        if (fileName.endsWith("AT2.BAM")) {
            animationName = ATTACK2;
        }
        if (fileName.endsWith("A3.BAM")) {
            animationName = ATTACK3;
        }
        if (fileName.endsWith("A4.BAM")) {
            animationName = ATTACK4;
        }
        if (fileName.endsWith("G22.BAM")) {
            animationName = ATTACK3;
        }
        if (fileName.endsWith("CA.BAM")) {
            animationName = CASTING1;
        }
        if (fileName.endsWith("G25.BAM")) {
            animationName = CASTING1;
        }
        if (fileName.endsWith("SP1.BAM")) {
            animationName = CASTING1;
        }
        if (fileName.endsWith("SP2.BAM")) {
            animationName = CASTING2;
        }
        if (fileName.endsWith("DE.BAM")) {
            animationName = DIE;
        }
        if (fileName.endsWith("DFB.BAM")) {
            animationName = DIE;
        }
        if (fileName.endsWith("GU.BAM")) {
            animationName = GETUP;
        }
        if (fileName.endsWith("GUP.BAM")) {
            animationName = GETUP;
        }
        if (fileName.endsWith("GH.BAM")) {
            animationName = HIT1;
        }
        if (fileName.endsWith("G13.BAM")) {
            animationName = HIT1;
        }
        if (fileName.endsWith("HIT.BAM")) {
            animationName = HIT1;
        }
        if (fileName.endsWith("G14.BAM")) {
            animationName = HIT2;
        }
        if (fileName.endsWith("G23.BAM")) {
            animationName = RANGEDATTACK1;
        }
        if (fileName.endsWith("G24.BAM")) {
            animationName = RANGEDATTACK2;
        }
        if (fileName.endsWith("RUN.BAM")) {
            animationName = RUN;
        }
        if (fileName.endsWith("SL.BAM")) {
            animationName = SLEEP;
        }
        if (fileName.endsWith("SP.BAM")) {
            animationName = SPELL;
        }
        if (fileName.endsWith("G26.BAM")) {
            animationName = SPELL;
        }
        if (fileName.endsWith("SD.BAM")) {
            animationName = STAND1;
        }
        if (fileName.endsWith("G1.BAM")) {
            animationName = STAND1;
        }
        if (fileName.endsWith("CF1.BAM")) {
            animationName = STAND1;
        }
        if (fileName.endsWith("STC.BAM")) {
            animationName = STAND1;
        }
        if (fileName.endsWith("SC.BAM")) {
            animationName = STAND2;
        }
        if (fileName.endsWith("G12.BAM")) {
            animationName = STAND2;
        }
        if (fileName.endsWith("C2S.BAM")) {
            animationName = STAND2;
        }
        if (fileName.endsWith("STD.BAM")) {
            animationName = STAND2;
        }
        if (fileName.endsWith("C2C.BAM")) {
            animationName = STAND3;
        }
        if (fileName.endsWith("SF1.BAM")) {
            animationName = STAND3;
        }
        if (fileName.endsWith("TK1.BAM")) {
            animationName = TALK1;
        }
        if (fileName.endsWith("TK2.BAM")) {
            animationName = TALK2;
        }
        if (fileName.endsWith("TW.BAM")) {
            animationName = TWITCH;
        }
        if (fileName.endsWith("G15.BAM")) {
            animationName = TWITCH;
        }
        if (fileName.endsWith("WK.BAM")) {
            animationName = WALK;
        }
        if (fileName.endsWith("G11.BAM")) {
            animationName = WALK;
        }
        if (fileName.endsWith("WLK.BAM")) {
            animationName = WALK;
        }

        if (fileName.endsWith("A1E.BAM")) {
            animationName = ATTACK1_EAST;
        }
        if (fileName.endsWith("G2E.BAM")) {
            animationName = ATTACK1_EAST;
        }
        if (fileName.endsWith("AT1E.BAM")) {
            animationName = ATTACK1_EAST;
        }
        if (fileName.endsWith("A2E.BAM")) {
            animationName = ATTACK2_EAST;
        }
        if (fileName.endsWith("G21E.BAM")) {
            animationName = ATTACK2_EAST;
        }
        if (fileName.endsWith("AT2E.BAM")) {
            animationName = ATTACK2_EAST;
        }
        if (fileName.endsWith("A3E.BAM")) {
            animationName = ATTACK3_EAST;
        }
        if (fileName.endsWith("A4E.BAM")) {
            animationName = ATTACK4_EAST;
        }
        if (fileName.endsWith("G22E.BAM")) {
            animationName = ATTACK3_EAST;
        }
        if (fileName.endsWith("CAE.BAM")) {
            animationName = CASTING1_EAST;
        }
        if (fileName.endsWith("G25E.BAM")) {
            animationName = CASTING1_EAST;
        }
        if (fileName.endsWith("SP1E.BAM")) {
            animationName = CASTING1_EAST;
        }
        if (fileName.endsWith("SP2E.BAM")) {
            animationName = CASTING2_EAST;
        }
        if (fileName.endsWith("DEE.BAM")) {
            animationName = DIE_EAST;
        }
        if (fileName.endsWith("DFBE.BAM")) {
            animationName = DIE_EAST;
        }
        if (fileName.endsWith("GUE.BAM")) {
            animationName = GETUP_EAST;
        }
        if (fileName.endsWith("GUPE.BAM")) {
            animationName = GETUP_EAST;
        }
        if (fileName.endsWith("GHE.BAM")) {
            animationName = HIT1_EAST;
        }
        if (fileName.endsWith("G13E.BAM")) {
            animationName = HIT1_EAST;
        }
        if (fileName.endsWith("HITE.BAM")) {
            animationName = HIT1_EAST;
        }
        if (fileName.endsWith("G14E.BAM")) {
            animationName = HIT2_EAST;
        }
        if (fileName.endsWith("G23E.BAM")) {
            animationName = RANGEDATTACK1_EAST;
        }
        if (fileName.endsWith("G24E.BAM")) {
            animationName = RANGEDATTACK2_EAST;
        }
        if (fileName.endsWith("RUNE.BAM")) {
            animationName = RUN_EAST;
        }
        if (fileName.endsWith("SLE.BAM")) {
            animationName = SLEEP_EAST;
        }
        if (fileName.endsWith("SPE.BAM")) {
            animationName = SPELL_EAST;
        }
        if (fileName.endsWith("G26E.BAM")) {
            animationName = SPELL_EAST;
        }
        if (fileName.endsWith("SDE.BAM")) {
            animationName = STAND1_EAST;
        }
        if (fileName.endsWith("G1E.BAM")) {
            animationName = STAND1_EAST;
        }
        if (fileName.endsWith("CF1E.BAM")) {
            animationName = STAND1_EAST;
        }
        if (fileName.endsWith("STCE.BAM")) {
            animationName = STAND1_EAST;
        }
        if (fileName.endsWith("SCE.BAM")) {
            animationName = STAND2_EAST;
        }
        if (fileName.endsWith("G12E.BAM")) {
            animationName = STAND2_EAST;
        }
        if (fileName.endsWith("C2SE.BAM")) {
            animationName = STAND2_EAST;
        }
        if (fileName.endsWith("STDE.BAM")) {
            animationName = STAND2_EAST;
        }
        if (fileName.endsWith("C2CE.BAM")) {
            animationName = STAND3_EAST;
        }
        if (fileName.endsWith("SF1E.BAM")) {
            animationName = STAND3_EAST;
        }
        if (fileName.endsWith("TK1E.BAM")) {
            animationName = TALK1_EAST;
        }
        if (fileName.endsWith("TK2E.BAM")) {
            animationName = TALK2_EAST;
        }
        if (fileName.endsWith("TWE.BAM")) {
            animationName = TWITCH_EAST;
        }
        if (fileName.endsWith("G15E.BAM")) {
            animationName = TWITCH_EAST;
        }
        if (fileName.endsWith("WKE.BAM")) {
            animationName = WALK_EAST;
        }
        if (fileName.endsWith("G11E.BAM")) {
            animationName = WALK_EAST;
        }
        if (fileName.endsWith("WLKE.BAM")) {
            animationName = WALK_EAST;
        }

        if (animationName.equals(defaultName)) {
            logger.warn("WARNING: could not find animation name for " + fileName + " using default name : " + defaultName);
        }

        return animationName;
    }

    public static Collection<File> getFiles(String directoryName, String filter) {
        File directory = new File(directoryName);
        return FileUtils.listFiles(directory, new WildcardFileFilter(filter), null);
    }

    public static byte[] getSubArray(byte[] buffer, int offset, int length) {
        byte[] r = new byte[length];
        System.arraycopy(buffer, offset, r, 0, length);
        return r;
    }

    public static byte[] mergeArrays(byte[] a1, byte[] a2) {
        byte[] r = new byte[a1.length + a2.length];
        System.arraycopy(a1, 0, r, 0, a1.length);
        System.arraycopy(a2, 0, r, a1.length, a2.length);
        return r;
    }

    public static byte[] resizeArray(byte[] src, int new_size) {
        byte[] tmp = new byte[new_size];
        System.arraycopy(src, 0, tmp, 0, Math.min(src.length, new_size));
        return tmp;
    }

    public static byte[] convertBack(byte value) {
        byte[] buffer = {value};
        return buffer;
    }

    public static byte[] convertBack(short value) {
        byte[] buffer = new byte[2];
        for (int i = 0; i <= 1; i++) {
            buffer[i] = ((byte) (value >> 8 * i & 0xFF));
        }
        return buffer;
    }

    public static byte[] convertBack(int value) {
        byte[] buffer = new byte[4];
        for (int i = 0; i <= 3; i++) {
            buffer[i] = ((byte) (value >> 8 * i & 0xFF));
        }
        return buffer;
    }

    public static byte[] convertBack(long value) {
        byte[] buffer = new byte[8];
        for (int i = 0; i <= 7; i++) {
            buffer[i] = ((byte) (int) (value >> 8 * i & 0xFF));
        }
        return buffer;
    }

    public static byte convertByte(byte[] buffer, int offset) {
        int value = 0;
        for (int i = 0; i >= 0; i--) {
            value = value << 8 | buffer[(offset + i)] & 0xFF;
        }
        return (byte) value;
    }

    public static int convertInt(byte[] buffer, int offset) {
        int value = 0;
        for (int i = 3; i >= 0; i--) {
            value = value << 8 | buffer[(offset + i)] & 0xFF;
        }
        return value;
    }

    public static long convertLong(byte[] buffer, int offset) {
        long value = 0L;
        for (int i = 7; i >= 0; i--) {
            value = value << 8 | buffer[(offset + i)] & 0xFF;
        }
        return value;
    }

    public static short convertShort(byte[] buffer, int offset) {
        int value = 0;
        for (int i = 1; i >= 0; i--) {
            value = value << 8 | buffer[(offset + i)] & 0xFF;
        }
        return (short) value;
    }

    public static String convertString(byte[] buffer, int offset, int length) {
        for (int i = 0; i < length; i++) {
            if (buffer[(offset + i)] == 0) {
                return new String(buffer, offset, i);
            }
        }
        return new String(buffer, offset, length);
    }

    public static byte[] convertThreeBack(int value) {
        byte[] buffer = new byte[3];
        for (int i = 0; i <= 2; i++) {
            buffer[i] = ((byte) (value >> 8 * i & 0xFF));
        }
        return buffer;
    }

    public static short convertUnsignedByte(byte[] buffer, int offset) {
        short value = (short) convertByte(buffer, offset);
        if (value < 0) {
            value = (short) (value + 256);
        }
        return value;
    }

    public static long convertUnsignedInt(byte[] buffer, int offset) {
        long value = convertInt(buffer, offset);
        if (value < 0L) {
            value += 4294967296L;
        }
        return value;
    }

    public static int convertUnsignedShort(byte[] buffer, int offset) {
        int value = convertShort(buffer, offset);
        if (value < 0) {
            value += 65536;
        }
        return value;
    }

    public static byte[] compress(byte[] data, String signature, String version) {
        byte[] header = mergeArrays(signature.getBytes(), version.getBytes());
        header = mergeArrays(header, convertBack(data.length));
        byte[] result = resizeArray(header, data.length * 2);
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        int clength = deflater.deflate(result, 12, result.length - 12);
        return getSubArray(result, 0, clength + 12);
    }

    public static byte[] decompress(byte[] buffer) throws IOException {

        Inflater inflater = new Inflater();
        byte[] result = new byte[convertInt(buffer, 8)];
        inflater.setInput(buffer, 12, buffer.length - 12);
        try {
            inflater.inflate(result);
        } catch (DataFormatException e) {
            throw new IOException();
        }
        inflater.reset();
        return result;
    }

    class Palette {

        private final int[] colors;

        Palette(byte[] buffer, int offset, int length) {
            this.colors = new int[length / 4];
            for (int i = 0; i < this.colors.length; i++) {
                this.colors[i] = convertInt(buffer, offset + i * 4);
            }
        }

        public int getColor(byte[] buffer, int offset, byte index) {
            if (index < 0) {
                return convertInt(buffer, offset + (256 + index) * 4);
            }
            return convertInt(buffer, offset + index * 4);
        }

        public int getColor(int index) {
            if (index < 0) {
                return this.colors[(index + 256)];
            }
            return this.colors[index];
        }

        public int[] getColorBytes(int index) {
            int color = getColor(index);
            int alpha = (color >> 24) & 0xff;
            int red = (color >> 16) & 0xFF;
            int green = (color >> 8) & 0xFF;
            int blue = color & 0xFF;
            int[] ret = {red, green, blue, alpha};
            return ret;
        }
    }

    class Bam {

        String bamFileName;
        Frame[] frames;
        Anim[] anims;
        int[] lookupTable;

        Bam(String name, Frame[] frames, Anim[] anims, int[] lt) {
            logger.debug("Creating BAM from: " + name);
            this.bamFileName = name;
            this.frames = frames;
            this.anims = anims;
            this.lookupTable = lt;
        }

        public Image getFrame(int frameNr) {
            return frames[frameNr].image;
        }

        public int getFrameNr(int animNr, int frameNr) {
            return lookupTable[(frameNr + this.anims[animNr].lookupIndex)];
        }
    }

    class Anim {

        int frameCount;
        int lookupIndex;

        Anim(byte[] buffer, int offset) {
            frameCount = convertShort(buffer, offset);
            lookupIndex = convertShort(buffer, offset + 2);
        }

        int getMaxLookup() {
            return frameCount + lookupIndex;
        }

    }

    class Frame {

        Image image;

        Frame(byte[] buffer, int offset) {

            int width = convertShort(buffer, offset);
            int height = convertShort(buffer, offset + 2);

            long frameDataOffset = convertUnsignedInt(buffer, offset + 8);
            boolean rle = true;
            if (frameDataOffset > Math.pow(2.0D, 31.0D)) {
                rle = false;
                frameDataOffset -= Math.pow(2.0D, 31.0D);
            }

            if ((height < 1) || (width < 1)) {
                return;
            }

            byte[] imagedata = new byte[height * width];

            if (!rle) {
                imagedata = getSubArray(buffer, (int) frameDataOffset, imagedata.length);
            } else {
                int w_idx = 0;
                while (w_idx < imagedata.length) {
                    byte b = buffer[((int) frameDataOffset++)];
                    imagedata[(w_idx++)] = b;
                    if (b == transparent) {
                        int toread = buffer[((int) frameDataOffset++)];
                        if (toread < 0) {
                            toread += 256;
                        }
                        for (int i = 0; i < toread; i++) {
                            if (w_idx < imagedata.length) {
                                imagedata[(w_idx++)] = transparent;
                            }
                        }
                    }
                }
            }

            ImageBuffer ib = new ImageBuffer(width, height);

            for (int h_idx = 0; h_idx < height; h_idx++) {
                for (int w_idx = 0; w_idx < width; w_idx++) {
                    int index = imagedata[(h_idx * width + w_idx)];
                    int rgb = palette.getColor(index);

                    if (index != transparent) {
                        rgb = (255 << 24) | (rgb & 0xffffff);
                    }

                    int alpha = (rgb >> 24) & 0xff;
                    int red = (rgb >> 16) & 0xFF;
                    int green = (rgb >> 8) & 0xFF;
                    int blue = rgb & 0xFF;

                    ib.setRGBA(w_idx, h_idx, red, green, blue, alpha);

                }
            }

            this.image = ib.getImage();

        }

    }

    public Image getConvertedSlickImage(BufferedImage image) throws Exception {
        Texture texture = BufferedImageUtil.getTexture("", image);
        Image slickImage = new Image(image.getWidth(), image.getHeight());
        slickImage.setTexture(texture);
        return slickImage;
    }

}
