package com.nuntiias.nuntiitheone;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

public class
RecyclerAdapter implements Parcelable// extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>
{

    Context context;
    private String rv_iataOrigin;
    private String rv_iataDestination;
    private String rv_dateDestination;
    private String rv_timeDestination;
    private String rv_airportDestination;
    private String rv_dateOrigin;
    private String rv_timeOrigin;
    private String rv_airportOrigin;
    private String rv_flightNumber;
    private String rv_originLat;
    private String rv_originLon;
    private String rv_destinationLat;
    private String rv_destinationLon;


    public RecyclerAdapter(Context context, String rv_iataOrigin, String rv_iataDestination, String rv_dateDestination, String rv_timeDestination, String rv_airportDestination, String rv_dateOrigin, String rv_timeOrigin, String rv_airportOrigin, String rv_flightNumber, String rv_originLat, String rv_originLon, String rv_destinationLat, String rv_destinationLon) {
        this.context = context;
        this.rv_iataOrigin = rv_iataOrigin;
        this.rv_iataDestination = rv_iataDestination;
        this.rv_dateDestination = rv_dateDestination;
        this.rv_timeDestination = rv_timeDestination;
        this.rv_airportDestination = rv_airportDestination;
        this.rv_dateOrigin = rv_dateOrigin;
        this.rv_timeOrigin = rv_timeOrigin;
        this.rv_airportOrigin = rv_airportOrigin;
        this.rv_flightNumber = rv_flightNumber;
        this.rv_originLat = rv_originLat;
        this.rv_originLon = rv_originLon;
        this.rv_destinationLat = rv_destinationLat;
        this.rv_destinationLon = rv_destinationLon;
    }

    protected RecyclerAdapter(Parcel in) {
        rv_iataOrigin = in.readString();
        rv_iataDestination = in.readString();
        rv_dateDestination = in.readString();
        rv_timeDestination = in.readString();
        rv_airportDestination = in.readString();
        rv_dateOrigin = in.readString();
        rv_timeOrigin = in.readString();
        rv_airportOrigin = in.readString();
        rv_flightNumber = in.readString();
        rv_originLat = in.readString();
        rv_originLon = in.readString();
        rv_destinationLat = in.readString();
        rv_destinationLon = in.readString();
    }

    public static final Creator<RecyclerAdapter> CREATOR = new Creator<RecyclerAdapter>() {
        @Override
        public RecyclerAdapter createFromParcel(Parcel in) {
            return new RecyclerAdapter(in);
        }

        @Override
        public RecyclerAdapter[] newArray(int size) {
            return new RecyclerAdapter[size];
        }
    };

    public String getRv_iataOrigin() {
        return rv_iataOrigin;
    }

    public String getRv_iataDestination() {
        return rv_iataDestination;
    }

    public String getRv_dateDestination() {
        return rv_dateDestination;
    }

    public String getRv_timeDestination() {
        return rv_timeDestination;
    }

    public String getRv_airportDestination() {
        return rv_airportDestination;
    }

    public String getRv_dateOrigin() {
        return rv_dateOrigin;
    }

    public String getRv_timeOrigin() {
        return rv_timeOrigin;
    }

    public String getRv_airportOrigin() {
        return rv_airportOrigin;
    }

    public String getRv_flightNumber() {
        return rv_flightNumber;
    }

    public String getRv_originLat() { return rv_originLat; }

    public String getRv_originLon() { return rv_originLon; }

    public String getRv_destinationLat() { return rv_destinationLat; }

    public String getRv_destinationLon() { return rv_destinationLon; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(rv_iataOrigin);
        dest.writeString(rv_iataDestination);
        dest.writeString(rv_dateDestination);
        dest.writeString(rv_timeDestination);
        dest.writeString(rv_airportDestination);
        dest.writeString(rv_dateOrigin);
        dest.writeString(rv_timeOrigin);
        dest.writeString(rv_airportOrigin);
        dest.writeString(rv_flightNumber);
        dest.writeString(rv_originLat);
        dest.writeString(rv_originLon);
        dest.writeString(rv_destinationLat);
        dest.writeString(rv_destinationLon);
    }
}

    /*@NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.rv_flight_information, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position)
    {

        holder.textView.setText(data[position]);
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "Clicked on " + data[position], Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView textView;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            textView = itemView.findViewById(R.id.iataCode);
        }
    }
}*/
