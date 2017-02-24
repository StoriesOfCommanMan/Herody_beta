package storiesofcommonman.in.herody_beta;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by WIN on 2/24/2017.
 */
public class SuggestionAdapter  extends RecyclerView.Adapter<SuggestionAdapter.NumberViewHolder>{
    private int n=0;
    private OlaDetails o;private uberDetails u;


    public SuggestionAdapter(int numberofitems,OlaDetails olaDetails,uberDetails ub)
    {
       n=numberofitems;o=olaDetails;u=ub;

    }

    @Override
    public NumberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.single_suggestion,parent,false);

        NumberViewHolder numberViewHolder=new NumberViewHolder(view);
        return numberViewHolder;
    }

    @Override
    public void onBindViewHolder(NumberViewHolder holder, int position) {
        switch (position)
        {
            case 0:
            {
                holder.t1.setText("OLA mini");
                holder.t2.setText(o.getEta()/60+" min away");
                holder.t3.setText(o.getMinAmount()+" INR - "+o.getMaxAmount()+" INR");
                holder.t4.setText(o.getTravelTime()+" min");
                break;

            }
            case 1:
            {
                holder.t1.setText("UBER go");
                holder.t2.setText(Integer.parseInt(u.getEta())/60+" min away");
                holder.t3.setText(u.getMinAmount()+" INR - "+u.getMaxAmount()+" INR");
                holder.t4.setText(u.getTravelTime()+" min");
                break;

            }
            case 2:
            {
                holder.t1.setText("Jugnoo Auto");
                holder.t2.setText("N/A");
                holder.t3.setText(o.getDistance()*25+" INR");
                holder.t4.setText(u.getTravelTime()+" mins");
                break;

            }
        }

    }

    @Override
    public int getItemCount() {
        return n;
    }

    public class NumberViewHolder extends RecyclerView.ViewHolder
    {
        private TextView t1,t2,t3,t4;
        public NumberViewHolder(View itemView) {
            super(itemView);
            t1=(TextView)itemView.findViewById(R.id.textView);
            t2=(TextView)itemView.findViewById(R.id.textView2);
            t3=(TextView)itemView.findViewById(R.id.textView3);
            t4=(TextView)itemView.findViewById(R.id.textView4);
        }
    }
}
