package com.evento.team2.eventspack.adapters;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import com.bumptech.glide.Glide;
import com.evento.team2.eventspack.R;
import com.evento.team2.eventspack.models.Category;
import com.evento.team2.eventspack.models.Event;
import com.evento.team2.eventspack.ui.activites.ActivityEventDetails;
import com.evento.team2.eventspack.utils.DateFormatterUtils;
import com.evento.team2.eventspack.utils.Utils;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Daniel on 15-Mar-16.
 */
public class CategoriesViewAdapter extends ExpandableRecyclerAdapter<
        CategoriesViewAdapter.CategoryViewHolder, CategoriesViewAdapter.EventViewHolder> {

    Context context;
    //    private List<Category> categoryList;
    private LayoutInflater inflater;

    public CategoriesViewAdapter(Context context, List<Category> parentItemList) {
        super(parentItemList);

        this.context = context;
//        categoryList = parentItemList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public CategoryViewHolder onCreateParentViewHolder(ViewGroup parentViewGroup) {
        View categoryView = inflater.inflate(R.layout.item_category, parentViewGroup, false);
        return new CategoryViewHolder(categoryView);
    }

    @Override
    public EventViewHolder onCreateChildViewHolder(ViewGroup childViewGroup) {
        View eventView = inflater.inflate(R.layout.item_small, childViewGroup, false);
        return new EventViewHolder(eventView);
    }

    @Override
    public void onBindParentViewHolder(CategoryViewHolder categoryViewHolder, int position, ParentListItem parentListItem) {
        Category eventCategory = (Category) parentListItem;
        categoryViewHolder.bind(eventCategory);
    }

    @Override
    public void onBindChildViewHolder(EventViewHolder eventViewHolder, int position, Object childListItem) {
        Event event = (Event) childListItem;
        eventViewHolder.bind(event);
    }

//    public void setCategoriesData(List<Category> categoryList) {
//        this.categoryList = categoryList;
//    }

    // TODO finish me
//    @Override
//    public long getItemId(int position) {
//        return categoryList.get(position).getChildItemList().ge;
//    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(true);
    }

    class CategoryViewHolder extends ParentViewHolder {

        private TextView categoryTextView;
        private ImageView categoryImageView;

        CategoryViewHolder(View itemView) {
            super(itemView);
            categoryTextView = (TextView) itemView.findViewById(R.id.category);
            categoryImageView = (ImageView) itemView.findViewById(R.id.category_image);
        }

        public void bind(Category category) {
            categoryTextView.setText(context.getResources().getString(category.categoryNameId));

            categoryImageView.setImageResource(Utils.fetchDrawableForCategory(category.categoryId));
        }

        @Override
        public boolean shouldItemViewClickToggleExpansion() {
            return true;
        }
    }

    class EventViewHolder extends ChildViewHolder {

        final View mView;
        ImageView mEventImage;
        TextView mEventTitle;

//        @BindView(R.id.event_details)
//        public TextView mEventDetails;
//        @BindView(R.id.event_start_time)
//        public TextView mEventStartTime;
//        @BindView(R.id.event_end_time)
//        public TextView mEventEndTime;
//        @BindView(R.id.event_location)
//        public TextView mEventLocation;
//        @BindView(R.id.btn_save_icon)
//        public IconTextView isEventSaved;
//        @BindView(R.id.eventAttending)
//        View mEventAttending;
//        @BindView(R.id.eventAttendingCount)
//        TextView mEventAttendingCount;


        EventViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            mEventTitle = ButterKnife.findById(itemView, R.id.event_title);
            mEventImage = ButterKnife.findById(itemView, R.id.small_event_picture);
        }

        public void bind(Event event) {
            mEventTitle.setText(event.name);

            ButterKnife.findById(mView, R.id.event_color).setVisibility(View.GONE);
            ((TextView) ButterKnife.findById(mView, R.id.event_title)).setText(event.name);
            ((TextView) ButterKnife.findById(mView, R.id.event_details)).setText(event.details);
            ((TextView) ButterKnife.findById(mView, R.id.event_time)).setText(DateFormatterUtils.fullDateFormat.format(event.startTimeStamp));

            if (TextUtils.isEmpty(event.pictureUri)) {
                Glide.with(mEventImage.getContext()).load(R.drawable.party_image).into(mEventImage);
            } else {
                Glide.with(mEventImage.getContext()).load(event.pictureUri).into(mEventImage);
            }

            mView.setOnClickListener(view -> {
                Intent intent = ActivityEventDetails.createIntent(context, event.id);

//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//                    ActivityOptions options = ActivityOptions
//                            .makeSceneTransitionAnimation((Activity) context, mEventImage, EventiConstants.TRANSITION_EVENT_IMAGE);
//
//                    context.startActivity(intent, options.toBundle());
//                } else {
                context.startActivity(intent);
//                }

            });
        }
    }
}