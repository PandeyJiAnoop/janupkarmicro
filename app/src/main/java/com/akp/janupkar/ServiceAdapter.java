package com.akp.janupkar;

//public class Demo extends AppCompatActivity {
//    private RecyclerView recyclerView;
//    private ServiceAdapter adapter;
//    private RequestQueue requestQueue;
//    private List<Service> serviceList;
//    TextView title_tv;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_demo);
//        title_tv= findViewById(R.id.title_tv);
//        recyclerView = findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        serviceList = new ArrayList<>();
//        adapter = new ServiceAdapter(this, serviceList);
//        recyclerView.setAdapter(adapter);
//
//        // Call the method to fetch the data using Volley
//        fetchData();
//    }
//
//
//
//    private void fetchData() {
//        String url = "https://eazypan.in/app/eServices"; // Replace with the actual API endpoint
//
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            int status = response.getInt("status");
//                            if (status == 1) {
//                                JSONObject eServicesList = response.getJSONObject("eServicesList");
//                                // Create a string to store the category names
//                                StringBuilder categoryNames = new StringBuilder();
//                                // Iterate through each service category
//                                Iterator<String> categoryIterator = eServicesList.keys();
//                                while (categoryIterator.hasNext()) {
//                                    String category = categoryIterator.next();
//
//                                    categoryNames.append(category);
//                                    if (categoryIterator.hasNext()) {
//                                        categoryNames.append(", ");
//                                    }
//
//                                    JSONArray categoryArray = eServicesList.getJSONArray(category);
//                                    // Iterate through each service item in the category
//                                    for (int i = 0; i < categoryArray.length(); i++) {
//                                        JSONObject serviceObject = categoryArray.getJSONObject(i);
//
//                                        // Create an EService object and set its values
//                                        Service eService = new Service();
//                                        eService.setId(serviceObject.getString("id"));
//                                        eService.setServiceType(serviceObject.getString("service_type"));
//                                        eService.setName(serviceObject.getString("name"));
//                                        eService.setUrl(serviceObject.getString("url"));
//                                        eService.setImage(serviceObject.getString("image"));
//                                        eService.setCreatedTime(serviceObject.getString("created_time"));
//                                        // Add the EService object to the list
//                                        serviceList.add(eService);
//
//                                    }
//                                }
//                                title_tv.setText(categoryNames.toString());
//
//                                // Notify the adapter that the data has changed
//                                adapter.notifyDataSetChanged();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        error.printStackTrace();
//                    }
//                });
//
//        // Add the request to the Volley request queue
//        RequestQueue queue = Volley.newRequestQueue(this);
//        queue.add(request);
//    }
//
//
//
//    public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {
//        private Context context;
//        private List<Service> serviceList;
//
//        public ServiceAdapter(Context context, List<Service> serviceList) {
//            this.context = context;
//            this.serviceList = serviceList;
//        }
//
//
//        @NonNull
//        @Override
//        public ServiceAdapter.ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(context).inflate(R.layout.item_service, parent, false);
//            return new ServiceAdapter.ServiceViewHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull ServiceAdapter.ServiceViewHolder holder, int position) {
//            Service service = serviceList.get(position);
//            holder.bind(service);
//        }
//
//        @Override
//        public int getItemCount() {
//            return serviceList.size();
//        }
//
//        public class ServiceViewHolder extends RecyclerView.ViewHolder {
//            private ImageView imageView;
//            private TextView nameTextView;
//
//            public ServiceViewHolder(@NonNull View itemView) {
//                super(itemView);
//                imageView = itemView.findViewById(R.id.imageView);
//                nameTextView = itemView.findViewById(R.id.textViewName);
//            }
//
//            public void bind(Service service) {
////                title_tv.setText(service.getServiceType());
//                nameTextView.setText(service.getName());
//                Glide.with(context).load(service.getImage()).into(imageView);
//
//                itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        // Handle item click if needed
//                    }
//                });
//            }
//        }
//    }
//}