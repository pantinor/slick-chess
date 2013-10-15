Slick Chess

Borrowed Java Chess code (from Arvydas Bancewicz) and refactored with the Slick opengl library.

Added animations using sprite sheets.
I used sprites from this website for the SPRITES image set: http://www.reinerstilesets.de/

You can switch the pieces by modifying ChessBoard.java program like this:

		//this.boardMedia = new BoardMedia(BoardMedia.Type.SPRITES);
		this.boardMedia = new BoardMedia(BoardMedia.Type.IMAGE);
		
You can switch the computer opponent manualy in the ChessGame.java here:

		whiteParameters = new PlayerParameters("White", true);
		blackParameters = new PlayerParameters("Black", false); //computer player
		
Original java chess source is here:
https://github.com/Arwid/chess

Maven Notes:

The Slick dependencies are not in a public Maven Nexus repository (not that I could find) so I patched them manually in my M2 local.
Check the pom.xml for the detail. 

I built slick2d dependency from the maven repo here:
https://github.com/nguillaumin/slick2d-maven








