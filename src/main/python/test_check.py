import pandas as pd

# -------------------------------------------------------------
# check the names with same netacuity-codes in both edge and pulse
# -------------------------------------------------------------
# df_netacuity_code_name_check = pd.merge(df_edge_city, df_pulse_city, left_on=['netacuity_id_x'], right_on=['CITY-CODE'], how='inner')
# df_netacuity_code_name_check['check_name'] = (df_netacuity_code_name_check['city_smaller_name_x'] == df_netacuity_code_name_check['city_smaller_name_y'])
# df_netacuity_code_name_check_temp = df_netacuity_code_name_check[['city_smaller_name_x', 'city_smaller_name_y', 'netacuity_id_x', 'CITY-CODE', 'short_name', 'REGION', 'check_name']]
# df_netacuity_code_name_check_temp.to_csv("output/temp.csv")
# print(len(df_netacuity_code_name_check))

# -------------------------------------------------------------
# create the city and region csv with it's netacuity_codes csv
# -------------------------------------------------------------
# df_pulse_read = pd.read_csv("input/new_pulse_cities.csv")


# -------------------------------------------------------------
# create list of the cities present in the convert_to_list csv
# -------------------------------------------------------------
df_convert_to_list = pd.read_csv("./input/convert_to_list.csv").drop_duplicates()
df_convert_to_list[['adv_platform_id']].drop_duplicates().to_csv('./output/adv_id.csv', index=False)
# df_city = pd.read_csv("./input/city_table_pulse_migration.csv")
# df_merge = pd.merge(df_city, df_convert_to_list, left_on=['targeting_tag'], right_on=['targeting_tag'], how='inner')
# print(df_merge.head(25))
# df_convert_to_list['city'] = df_convert_to_list['city'].str.lower()
# print(df_convert_to_list.groupby('city').first())
# print(df_convert_to_list['city'].values.tolist())
# result_dict = pd.Series(df_convert_to_list.tag.values, index=df_convert_to_list.city).to_dict()
# print(result_dict)
# list_city = df_convert_to_list['city'].str.lower().values.tolist()
# df_first = pd.read_csv('output/edge_cities_not_in_pulse.csv')
# df_first = df_first.loc[df_first['name'].isin(list_city)]
# print(len(df_first))



# -------------------------------------------------------------
# combine the city code parts and get the combined list.
# -------------------------------------------------------------
print("-----------------------------")
df_temp = pd.read_csv('./input/test/check_city.csv')
df_temp['city'] = df_temp['city'].str.lower()
df_temp = df_temp[df_temp['state'].notna()]
df_temp['state'] = df_temp['state'].str.lower().replace({'ut': 'uk', 'tg': 'ts', 'ct': 'cg', 'or': 'od', 'dd': 'dh'})

df_city_code = pd.read_csv('./input/test_pulse_city_code.csv')
df_combined_city = pd.merge(df_temp, df_city_code, left_on=['city', 'state'], right_on=['CITY-NAME', 'REGION'], how='left')
# df_combined_city['check_']
# print(len(df_combined_city))
# df_combined_city.to_csv('./input/test/first.csv')


# -------------------------------------------------------------
# campaigns that are impacted
# -------------------------------------------------------------
df_active_campaigns = pd.read_csv("input/active_campaigns_city_targeting_latest.csv")
df_edge_cities_not_in_pulse = pd.read_csv("output/edge_cities_not_in_pulse.csv")
df_impacted_adsets = pd.merge(df_active_campaigns, df_edge_cities_not_in_pulse,
                              left_on=['city'], right_on=['name'], how='inner')
# print(len(df_impacted_adsets))

df_impacted_adsets = df_impacted_adsets[["demand_source","campaign_id","campaign_name","adset_id","adset_name","adset_end_date","city","primary_status","include"]]
# df_impacted_adsets.to_csv("output/impacted_adsets_for_not_in_pulse_cities.csv")

df_impacted_campaigns = df_impacted_adsets.filter(items=['campaign_id', 'campaign_name']).drop_duplicates()
# df_impacted_campaigns.to_csv("output/impacted_adsets_for_not_in_pulse_cities.csv", index=False)
# print(len(df_impacted_adsets[['campaign_id','campaign_name']].drop_duplicates()))
# print(len(df_impacted_adsets[['city']].drop_duplicates()))

# -----------------
# location_cluster impacted camapigns
# -----------------
df_active_cluster_campaigns = pd.read_csv('input/active_campaigns_cluter_targeting.csv')
df_active_cluster_campaigns = df_active_cluster_campaigns[["demand_source","campaign_id","campaign_name","adset_id","adset_name","adset_end_date","location_cluster","primary_status","include"]]
# df_active_cluster_campaigns.to_csv("output/impacted_adsets_for_location_cluster.csv")

df_active_cluster_campaigns = df_active_cluster_campaigns.filter(items=['campaign_id', 'campaign_name']).drop_duplicates()
# df_active_cluster_campaigns.to_csv("output/impacted_adsets_for_location_cluster.csv", index=False)
# print(len(df_active_cluster_campaigns[['campaign_id','campaign_name']].drop_duplicates()))


# -----------------
# checking targeting tags of the whole data
# -----------------
# df = pd.read_csv('input/test/first.csv')
# df['targeting_tag'] = 'CITY_' + df['city'].apply(lambda x: x.upper().replace(" ", "_"))
# print(df.head())
#
# df_city_table = pd.read_csv('input/city_table.csv')
# df_check_targeting_tag = pd.merge(df, df_city_table,
#                               left_on=['targeting_tag'], right_on=['targeting_tag'], how='inner')
# print(len(df_check_targeting_tag))


# -----------------
# checking difference in pp and prod data
# -----------------
# df_pp = pd.read_csv('input/test/pp_city_table.csv')
# df_prod = pd.read_csv('input/city_table_pulse_migration.csv')
#
# pp_cities_tag = df_pp['targeting_tag'].values.tolist()
# df_city_not_pp = df_prod.loc[~df_prod['targeting_tag'].isin(pp_cities_tag)]
# city_insert_statement_sample = "INSERT INTO adtech_master_data.city (name, netacuity_id, targeting_tag, state_id) VALUES('{0}', {1}, '{2}', {3});"
# for row in df_city_not_pp.itertuples():
#     temp = city_insert_statement_sample.format(row.name, row.netacuity_id, row.targeting_tag, row.state_id)
#     print(temp)
# print(df_city_not_pp.head(13))