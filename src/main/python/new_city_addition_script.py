import pandas as pd

# file = open("output/new_city_insert", "w")

df_new_city_list = pd.read_csv('input/test/first.csv')
location_cluster_edge_list= ['hosur', 'anekal', 'bidadi', 'hoskote', 'kanakapura', 'kengeri', 'nandi', 'nelamangala', 'sarjapur', 'whitefield', 'yelahanka', 'bagalur', 'new delhi', 'delhi', 'alipur', 'badarpur', 'bawana', 'chattarpur', 'connaught place', 'darya ganj', 'jharoda kalan', 'kalkaji', 'karol bagh', 'mehrauli', 'najafgarh', 'narela', 'shahdara', 'samalkha', 'secunderabad', 'begampet', 'gandipet', 'gangaram', 'kukatpally', 'medchal', 'nizampet', 'patancheru', 'trimulgherry', 'narsapur', 'kalyan city', 'ulhasnagar', 'bhiwandi', 'belapur', 'colaba', 'dharavi', 'goregaon', 'juhu', 'kalwa', 'kurla', 'madh', 'mahim', 'palghar', 'parel', 'sion', 'tardeo', 'trombay', 'vikhroli', 'virar', 'wadala', 'sanpada', 'alandi', 'aundh', 'chakan', 'chinchwad', 'khadki', 'koregaon', 'loni kalbhor', 'pimple', 'pirangut', 'barrackpore', 'barasat', 'kamarhati', 'bally', 'baranagar', 'behala', 'alipore', 'bankra', 'belgharia', 'hooghly', 'howrah', 'konnagar', 'tollygunge', 'uttarpara', 'baruipur', 'bidhannagar', 'new town', 'rajarhat gopalpur', 'tiruvottiyur', 'avadi', 'ambattur', 'tambaram', 'adyar', 'choolai', 'chromepet', 'kilpauk', 'kodambakkam', 'madipakkam', 'mylapore', 'nungambakkam', 'perambur', 'porur', 'pudur', 'royapettah', 'sriperumbudur', 'teynampet', 'vadapalani', 'villivakkam', 'chetput', 'bangalore', 'hyderabad', 'mumbai', 'pune', 'calcutta', 'chennai', 'thane', 'boisar', 'dahanu', 'ghansoli', 'malad', 'andheri', 'tumkur', 'channapatna', 'malur', 'sidlaghatta', 'magadi', 'faridabad city', 'gurgaon', 'ghaziabad', 'noida', 'bawana', 'bahadurgarh', 'baghpat', 'dadri', 'dasna', 'khekra', 'loni', 'mahbubnagar', 'bhongir', 'gudur', 'jangaon', 'medak', 'patancheru', 'siddipet', 'vikarabad', 'bhor', 'jejuri', 'rajgurunagar', 'shirwal', 'talegaon dabhade', 'vadgaon', 'manchar', 'uluberia', 'bankra', 'krishnapur', 'srirampur', 'santosh pur', 'alandur', 'pallavaram', 'ponneri', 'shadnagar', 'sohna', 'dhapa', 'allapur', 'jhajjar', 'sainikpuri', 'alibag', 'chakpanchuria', 'khalapur', 'begampur', 'gachibowli', 'michealnagar', 'sampla', 'naihati', 'haripal', 'hasnabad', 'sultanganj', 'shyamnagar', 'hatiara', 'korachandigarh', 'poonamallee', 'gummidipundi', 'adraspalli', 'ganganagar', 'subhas gram', 'halishahar', 'chandannagar', 'sahibabad', 'daund', 'abad kuliadanga', 'adavale budruk', 'jaskhar', 'vashi', 'kanchipuram', 'ballabgarh', 'vandalur', 'chandanagar', 'sarada', 'chandra', 'karjat', 'kalyani', 'rishra', 'barrackpore rs', 'tiruvallur', 'bagnan', 'neral', 'perungudi', 'apta', 'khopoli', 'gautam buddha nagar', 'agapalli', 'hridaypur', 'alwal', 'worli', 'sonipat', 'annaram', 'panvel', 'sewri', 'habra', 'serampore', 'sikandrabad', 'akanda danga', 'navalur', 'baidyabati', 'nisa hakimpet', 'amtem', 'madanpur', 'manovikasnagar', 'sector 4', 'nimta']
df_temp = df_new_city_list.loc[df_new_city_list['city'].isin(location_cluster_edge_list)]
print(len(df_temp))
print(df_temp.head())


df_edge_state = pd.read_csv("input/state_table.csv", delimiter =',')
df_edge_state['short_name'] = df_edge_state['short_name'].str.lower().replace({'ut': 'uk', 'tg': 'ts', 'ct': 'cg', 'or': 'od', 'dd': 'dh'})
df_new_city_list = pd.merge(df_new_city_list, df_edge_state, left_on=['state'], right_on=['short_name'], how='inner')

df_city_not_in_edge = pd.read_csv('output/new_pulse_cities_not_in_edge.csv')

new_city_list = df_city_not_in_edge['city_name'].values.tolist()
city_insert_statement_sample = "INSERT INTO adtech_master_data.city (name, netacuity_id, targeting_tag, state_id) VALUES('{0}', {1}, '{2}', {3});"
columns = ['name', 'netacuity_id', 'targeting_tag', 'state_id']
df_city_insert = pd.DataFrame(columns=columns)

for city in new_city_list:
    if(city=='malaiyanur' or city=='barrackpur - ii'):
        continue
    df_city = df_new_city_list.loc[df_new_city_list['city'] == city]
    name = city.title()    # print(df_city.head())
    netacuity_id = int(df_city.iloc[0, 5])
    targeting_tag = 'CITY_' + name.upper().replace(" ", "_")
    state_id = int(df_city.iloc[0, 6])
    insert_statement = city_insert_statement_sample.format(name, netacuity_id, targeting_tag, state_id)
    # file.write(insert_statement)
    # file.write("\n")

    row = [name, netacuity_id, targeting_tag, state_id]
    df_city_insert = pd.concat([df_city_insert, pd.DataFrame([row], columns=columns)], ignore_index=True)
    # print(insert_statement)

row_with_netacuity_id_0 = [['Malaiyanur', 0, 'CITY_MALAIYANUR', 52010036], ['Barrackpur - Ii', 0, 'CITY_BARRACKPUR_-_II', 52001309]]
for row in row_with_netacuity_id_0:
    df_city_insert = pd.concat([df_city_insert, pd.DataFrame([row], columns=columns)], ignore_index=True)

df_city_insert.to_csv("output/new_city_insert_automation.csv", sep='|', index=False)

# update of state code for the 164 cities or should we introduce them as new city?
state_change_edge_cities = ['chapra', 'shivpuri', 'burhanpur', 'bhadrachalam', 'dharmavaram', 'ganapavaram', 'gannavaram', 'gudur', 'kovur', 'nandigama', 'ramachandrapuram', 'repalle', 'goalpara', 'hajipur', 'lalganj', 'madhupur', 'alipur', 'badarpur', 'chandor', 'dwarka', 'bilaspur', 'jeori', 'fatehabad', 'gorakhpur', 'mustafabad', 'nagina', 'kollur', 'mallapur', 'sangam', 'siddapur', 'yellapur', 'durgapur', 'hingoli', 'kalwa', 'rajaram', 'amba', 'harda', 'maharajpur', 'narsinghpur', 'sarai', 'khurda', 'sonepur', 'fatehpur', 'jalalabad', 'rajpura', 'rampura', 'bali', 'baran', 'bari', 'dungarpur', 'nagar', 'partapur', 'rajgarh', 'mangalam', 'hasanpur', 'tanda', 'haldia', 'hasnabad', 'jamalpur', 'raiganj', 'rangamati', 'rudrapur', 'anuppur', 'melur', 'manpur', 'cumbum', 'mohali', 'aurangabad', 'gopalganj', 'kashipur', 'sheopur', 'umaria', 'atchanta', 'kannapuram', 'narsapur', 'baghmara', 'bishnupur', 'bajitpur', 'darap', 'mohanpur', 'radhanagar', 'badgaon', 'balapur', 'bangaon', 'semra', '7th mile kalimpong', 'ahmadpur', 'argora', 'ramnagar', 'bahadurpur', 'gaura', 'ghosi', 'khajuri', 'kharagpur', 'kundah', 'mobarakpur', 'baroda', 'basudevpur', 'shankarpur', 'bharatpur', 'balrampur', 'banda', 'bhainsa', 'dariapur', 'bhawanipur', 'bilari', 'deogaon', 'dharampur', 'gandhinagar', 'ganeshpur', 'basar', 'jairampur', 'hatia', 'islampur', 'jagatpur', 'karanji', 'barampur', 'lalpur', 'muradpur', 'andheri', 'deoli', 'gangapur', 'rajnagar', 'rampur', 'harinagar', 'mubarikpur', 'jalalpur', 'kanakpur', 'sarada', 'bhagwanpur', 'haripur', 'sultanpur', 'tarapur', 'adampur', 'madanpur', 'nagla', 'ramgarh', 'salarpur', 'shahabad', 'thana', 'alampur', 'amgachi', 'amarpur', 'asansol', 'anandpur', 'andhari', 'babupur', 'ashoknagar', 'bargaon', 'binodpur', 'bagaha', 'jagannathpur', 'kandra', 'karanpura', 'mandal', 'balia', 'ballia', 'barail', 'barharwa', 'basantpur', 'bhimpur', 'chandi', 'khanpur', 'chatra']
city_update_statement_sample = "UPDATE adtech_master_data.city SET netacuity_id={0}, state_id={1} WHERE targeting_tag='{2}';"

# file.write("\n-------------------state change edge cities -------------\n")
for city in state_change_edge_cities:
    if(city=='atchanta' or city=='khurda' or city=='7th mile kalimpong'):
        continue
    df_city = df_new_city_list.loc[df_new_city_list['city'] == city]
    name = city.title()    # print(df_city.head())
    netacuity_id = int(df_city.iloc[0, 5])
    targeting_tag = 'CITY_' + name.upper().replace(" ", "_")
    state_id = int(df_city.iloc[0, 6])
    update_statement = city_update_statement_sample.format(netacuity_id, state_id, targeting_tag)
    # file.write(update_statement)
    # file.write("\n")

    row = [name, netacuity_id, targeting_tag, state_id]
    df_city_insert = pd.concat([df_city_insert, pd.DataFrame([row], columns=columns)], ignore_index=True)

# df_city_insert.to_csv("output/new_city_insert_automation.csv", sep='|', index=False)