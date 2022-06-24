
public class SendWhatsappMsg {

	
		// TODO Auto-generated method stub
		/*Intent sendIntent = new Intent("android.intent.action.SEND");
        File f=new File("path to the file");
        Uri uri = Uri.fromFile(f);
        sendIntent.setComponent(new ComponentName("com.whatsapp","com.whatsapp.ContactPicker"));
        sendIntent.setType("image");
        sendIntent.putExtra(Intent.EXTRA_STREAM,uri);
        sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators("919xxxxxxxxx")+"@s.whatsapp.net");
        sendIntent.putExtra(Intent.EXTRA_TEXT,"sample text you want to send along with the image");
        startActivity(sendIntent);*/
		
		
		 static {
			    try {
			    	System.load("D:/Software/msvcp/msvcp140.dll");
			    	System.out.println("Native code library loaded.\n");
			    } catch (UnsatisfiedLinkError e) {
			      System.err.println("Native code library failed to load.\n" + e);
			      System.exit(1);
			    }
			  }

			  public static void main(String argv[]) 
			  {
			   /* CkZip zip = new CkZip();
			    System.out.println(zip.version());  */  
			  }
	}


