import pandas as pd
import json
import os

city_groups_present = ['mumbai', 'chennai', 'delhi-ncr', 'kolkata', 'bangalore', 'hyderabad', 'agra', 'varanasi', 'allahabad',
                       'visakhapatnam', 'nagpur', 'amritsar', 'ludhiana', 'ahmedabad', 'dehradun', 'pune', 'bhubaneswar', 'patna',
                       'indore', 'bhopal', 'ranchi', 'surat', 'kanpur', 'kochi', 'nashik']
city_group_mapping = {'mumbai': 'M_MUM', 'chennai': 'M_CHE', 'delhi-ncr': 'M_NCR', 'kolkata': 'M_KOL', 'bangalore': 'M_BEN',
                      'hyderabad': 'M_HYD', 'agra': 'M_ALAGVA', 'varanasi': 'M_ALAGVA', 'allahabad': 'M_ALAGVA', 'visakhapatnam': 'M_VISVIJ',
                      'nagpur': 'M_NAG', 'amritsar': 'M_AMLU', 'ludhiana': 'M_AMLU', 'ahmedabad': 'M_AHM', 'dehradun': 'M_DUN',
                      'pune': 'M_PUN', 'bhubaneswar': 'M_BHU', 'patna': 'M_1PT', 'indore': 'M_1PT', 'bhopal': 'M_1PT', 'ranchi': 'M_1PT',
                      'surat': 'M_1PT', 'kanpur': 'M_1PT', 'kochi': 'M_ERN', 'nashik': 'M_NAG'}

with open('./input/ssai_mapping/current_mapping.json', 'r') as file:
    data = json.load(file)
df_old = pd.DataFrame(list(data.items()), columns=['city', 'city_group_tag'])
df_old['city_group_tag'] = 'M_'+ df_old['city_group_tag']
df_old_1 = df_old
print(len(df_old), '    old ssai mapping length')
# print(df_old.groupby(['city_group_tag']).agg('count').head(40))

df_new_ssai_city_mapping = pd.read_csv('./input/ssai_mapping/ssai_city_group_mapping.csv')
df_new_ssai_city_mapping['citygroup'] = df_new_ssai_city_mapping['citygroup'].str.lower()
df_new_ssai_city_mapping['city'] = df_new_ssai_city_mapping['city'].str.lower()
# df_new_ssai_city_mapping = df_new_ssai_city_mapping.loc[df_new_ssai_city_mapping['citygroup'].isin(city_groups_present)]
# df_new_ssai_city_mapping = df_new_ssai_city_mapping.loc[~df_new_ssai_city_mapping['citygroup'].isin(city_groups_present)]
print(len(df_new_ssai_city_mapping))
df_new_ssai_city_mapping['citygroup_tag'] = df_new_ssai_city_mapping['citygroup'].map(city_group_mapping)
# df_new_ssai_city_mapping = df_new_ssai_city_mapping.fillna('M_NA')
# print(df_new_ssai_city_mapping.loc[df_new_ssai_city_mapping['city']=='jaipur'].head())

df_new_ssai_city_mapping = df_new_ssai_city_mapping[['city','citygroup', 'citygroup_tag']]
df_new_ssai_city_mapping = df_new_ssai_city_mapping.drop_duplicates() #drop duplicates (3 duplicates)
df_new_ssai_city_mapping['city'] = df_new_ssai_city_mapping['city'].str.replace(' ', '_', regex=False)
print(len(df_new_ssai_city_mapping), '--------')
# print(df_new_ssai_city_mapping.groupby(['citygroup']).agg('count').head(35))
temp = df_new_ssai_city_mapping.groupby(['city']).size().reset_index(name='count')
# print(temp.loc[temp['count']>1].head()) #badarpur    bankra      tollygunge
# df_common_city_old = df_new_ssai_city_mapping.loc[df_new_ssai_city_mapping['city'] == 'dehradun']

# ----------------
# in current mapping what are changing
# ------------------
new_city_list = df_new_ssai_city_mapping['city'].str.lower().tolist()
print(len(new_city_list), '     ', len(set(new_city_list)))
df_old_common = df_old.loc[df_old['city'].isin(new_city_list)]
print(len(df_old_common))
# print(df_old['city'].str.lower().tolist(), '------') #20 cities not there in current cluster --- 120 are common

df_common_city = pd.merge(df_new_ssai_city_mapping, df_old, left_on=['city', 'citygroup_tag'],
                          right_on=['city', 'city_group_tag'], how='inner')
temp = df_old.loc[~df_old['city'].isin(df_common_city['city'].values.tolist())]
print(len(temp))
# print(temp.head(20))
temp_1 = df_old_common.loc[df_old_common['city'].isin(temp['city'].values.tolist())]
# print(temp_1)
print(len(df_common_city))

df_cities_not_in_current_mapping = df_new_ssai_city_mapping.loc[~df_new_ssai_city_mapping['city'].isin(df_common_city['city'].values.tolist())]
print(len(df_cities_not_in_current_mapping), '    ----')
# print(df_cities_not_in_current_mapping.head())

# df_cities_not_in_current_mapping_1 = pd.Series(df_cities_not_in_current_mapping.citygroup_tag.values, index=df_cities_not_in_current_mapping.city).to_dict()
# temp = df_new_ssai_city_mapping.loc[df_new_ssai_city_mapping['city']== 'majura taluka']
# print(temp)
df_cities_not_in_current_mapping['citygroup_tag'] = df_cities_not_in_current_mapping['citygroup_tag'].str.replace('^M_', '', regex=True)
# print(df_new_ssai_city_mapping.head())
# df_new_ssai_city_mapping_1 = pd.Series(df_cities_not_in_current_mapping.citygroup_tag.values, index=df_cities_not_in_current_mapping.city).to_dict()
# file_path = './output/ssai_mapping/new_ssai_mapping_phase_2.json'
# os.makedirs(os.path.dirname(file_path), exist_ok=True)
# with open(file_path, 'w') as f:
#     json.dump(df_new_ssai_city_mapping_1, f, indent=4)

# ---------------
# check the current cities with cities present in mds
# ---------------
df_old_1 = df_new_ssai_city_mapping
# df_edge_city = pd.read_csv("./input/test/first.csv")
df_edge_city = pd.read_csv("./input/city_table_pulse_migration.csv")
df_edge_city['name'] = df_edge_city['name'].str.replace(' ', '_')
mds_city_list = df_edge_city['name'].str.lower().tolist()
# print(mds_city_list)
# #
df_old_1['city'] = df_old_1['city'].replace('_', ' ')
df_check_city_present_in_mds =df_old_1.loc[~df_old_1['city'].isin(mds_city_list)]
print(len(df_check_city_present_in_mds), '    <--->')
# print(df_check_city_present_in_mds.head())

# ---------------
# check if the new added cities and existing cities are having common of 131 cities or not?
# ---------------
with open('./input/ssai_mapping/current_mapping.json', 'r') as file:
    data = json.load(file)
df_old = pd.DataFrame(list(data.items()), columns=['city', 'city_group_tag']).drop_duplicates()
# df_old['city_group_tag'] = 'M_'+ df_old['city_group_tag']

with open('./output/ssai_mapping/new_ssai_mapping_phase_2.json', 'r') as file:
    data = json.load(file)
df_new = pd.DataFrame(list(data.items()), columns=['city', 'city_group_tag'])
# df_new['city_group_tag'] = df_old['city_group_tag']
# print(df_new.groupby(['city_group_tag']).agg('count').head(40))
df_new['city_group_tag'] = 'M_'+ df_new['city_group_tag']
df_new['map_json'] = '{"property_key": "net.acuity.city.' + df_new['city'] + '", "property_value": "' + df_new['city_group_tag'] + '"}'
# df_new['map_json'].to_csv("./output/ssai_mapping/uss.csv", index=False)

# file_path = './output/ssai_mapping/snapshot.json'
# os.makedirs(os.path.dirname(file_path), exist_ok=True)
# with open(file_path, 'w') as f:
#     json.dump(df_new_ssai_city_mapping_1, f, indent=4)

#
# df_com = pd.merge(df_old, df_new, left_on=['city', 'city_group_tag'],  right_on=['city', 'city_group_tag'], how='inner').drop_duplicates()
df_com = pd.merge(df_old, df_new, left_on=['city'],  right_on=['city'], how='inner').drop_duplicates()
df_old['map_json'] = '{"property_key": "net.acuity.city.' + df_old['city'] + '"}'
# df_old['map_json'].to_csv("./output/ssai_mapping/remove_uss.csv", index=False)
print(len(df_com))
# print(df_com.head())
# temp_2 = df_old.loc[~df_old['city'].isin(df_com['city'].values.tolist())]
# print(temp_2)

df_old['check_netacuity_tag'] = 'net.acuity.city.' + df_old['city']
df_check = pd.read_csv('./input/ssai_mapping/need_to_remove_current_ssai.csv')
df_check_merge = pd.merge(df_old, df_check, left_on=['check_netacuity_tag'],  right_on=['tag'], how='inner')
print(len(df_check_merge))


# ---------------------IPL mappings------------------------
city_groups_present_new = ['agra', 'varanasi', 'allahabad', 'amritsar', 'ludhiana', 'patna','indore', 'bhopal', 'ranchi',
                           'surat', 'kanpur', 'nashik', 'coimbatore']

city_group_mapping_new = {'mumbai': 'M_MUM', 'chennai': 'M_CHE', 'delhi-ncr': 'M_NCR', 'kolkata': 'M_KOL', 'bangalore': 'M_BEN','hyderabad': 'M_HYD',
                          'agra': 'M_AGR', 'varanasi': 'M_VAR', 'allahabad': 'M_ALD', 'visakhapatnam': 'M_VISVIJ','nagpur': 'M_NAG',
                          'amritsar': 'M_AMR', 'ludhiana': 'M_LUD', 'ahmedabad': 'M_AHM', 'dehradun': 'M_DUN', 'pune': 'M_PUN', 'bhubaneswar': 'M_BHU',
                          'patna': 'M_PAT', 'indore': 'M_IN', 'bhopal': 'M_BHO', 'ranchi': 'M_RAN', 'surat': 'M_SUR', 'kanpur': 'M_KAN',
                          'kochi': 'M_ERN', 'nashik': 'M_NAS', 'aurangabad': 'M_AUR','coimbatore': 'M_COI','guwahati': 'M_GUW','madurai': 'M_MAD',
                          'vadodara': 'M_VAD','jaipur': 'M_JAI','lucknow': 'M_LKO'}

with open('./input/ssai_mapping/current_mapping_3.json', 'r') as file:
    data = json.load(file)
df_old = pd.DataFrame(list(data.items()), columns=['city', 'city_group_tag'])
df_old['city_group_tag'] = 'M_'+ df_old['city_group_tag']
df_old_1 = df_old
print(len(df_old), '    old ssai mapping length')

df_new_ssai_city_mapping_1 = pd.read_csv('./input/ssai_mapping/new_updates.csv')
df_new_ssai_city_mapping_1['citygroup'] = df_new_ssai_city_mapping_1['citygroup'].str.lower()
df_new_ssai_city_mapping_1['city'] = df_new_ssai_city_mapping_1['city'].str.lower()
df_new_ssai_city_mapping_1['city'] = df_new_ssai_city_mapping_1['city'].str.replace(' ', '_', regex=False)

df_1_updated = df_old.merge(df_new_ssai_city_mapping_1[['city', 'city_group_tag']], on='city', how='left', suffixes=('', '_new'))
df_1_updated['city_group_tag'] = df_1_updated['city_group_tag_new'].combine_first(df_1_updated['city_group_tag'])
df_1_updated = df_1_updated.drop(columns=['city_group_tag_new'])
df_1_updated['city_group_tag'] = df_1_updated['city_group_tag'].str.replace('^M_', '', regex=True)
print(df_1_updated.head())

df_1_updated = pd.Series(df_1_updated.city_group_tag.values, index=df_1_updated.city).to_dict()
file_path = './output/ssai_mapping/new_ssai_mapping_phase_3.json'
os.makedirs(os.path.dirname(file_path), exist_ok=True)
with open(file_path, 'w') as f:
    json.dump(df_1_updated, f, indent=4)