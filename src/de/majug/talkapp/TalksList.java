package de.majug.talkapp;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TalksList extends ListActivity {

	private final static String MIXXT_RSS = "http://jug-mannheim.mixxt.de/api/rss/events/new_events";

	private TalkAdapter adapter;

	private class TalkAdapter extends ArrayAdapter<SyndEntry> {

		Activity context;

		public TalkAdapter(Activity context) {
			super(context, R.layout.talkrow);
			this.context = context;
			// TODO Auto-generated constructor stub
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = context.getLayoutInflater();
			convertView = inflater.inflate(R.layout.talkrow, null, true);
			((TextView) convertView.findViewById(R.id.titel)).setText(getItem(
					position).getTitle());
			String content = ((SyndContent) getItem(position).getContents()
					.get(0)).getValue();
			// ((WebView) convertView.findViewById(R.id.htmlcontent)).loadData(
			// content.substring(0, content.length() > 750 ? 750
			// : content.length()), "text/html", "utf-8");
			((WebView) convertView.findViewById(R.id.htmlcontent))
					.loadDataWithBaseURL(null, content.substring(0,
							content.length() > 750 ? 750 : content.length()),
							"text/html", "UTF-8", null);
			return convertView;
		}

	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		adapter = new TalkAdapter(this);
		this.setListAdapter(adapter);
		fetchTalks();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Toast.makeText(this,
				adapter.getItem(position).getUri(),
				Toast.LENGTH_SHORT).show();
	}

	private void fetchTalks() {
		try {
			URL url = new URL(MIXXT_RSS);
			XmlReader.setDefaultEncoding("utf-8");
			SyndFeedInput input = new SyndFeedInput();
			SyndFeed feed = input.build(new XmlReader(url));
			@SuppressWarnings("unchecked")
			List<SyndEntry> entries = feed.getEntries();
			Toast.makeText(this, "#Feeds retrieved: " + entries.size(),
					Toast.LENGTH_SHORT);
			for (SyndEntry entry : entries) {
				Log.d("meine", entry.getTitle());
				adapter.add(entry);
			}
			adapter.notifyDataSetChanged();
		} catch (MalformedURLException e) {
			// TODO Wie soll ich jetzt eigentlich eine Standard-Fehler-Window
			// anzeigen?
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FeedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}