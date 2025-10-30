import pandas as pd

file = open("output/new_location_cluster_insert", "w")
file_state_check = open("output/test_city_code_list", "w")

# chennai = 134, hyd = 316, pune = 172, mum = 129, ban = 61, delhi = 101, cal = 355, total = 1268
chennai_cluster_city_list = ['chennai', 'tiruvallur', 'kanchipuram', 'krishnanagar', 'vellore', 'kannur', 'gandhinagar', 'ramakrishnapuram', 'avadi', 'ramanathapuram', 'vellanur', 'mugalivakkam', 'porur', 'nehrunagar', 'kattupakkam', 'perungudi', 'kalkulam', 'tiruvannamalai', 'ashoknagar', 'kovalam', 'krishnapuram', 'ayanambakkam', 'radhanagar', 'guindy', 'nagar', 'mettupalayam', 'venkatapuram', 'mangadu', 'tambaram', 'perumbakkam', 'ratnagiri', 'ponniammanmedu', 'tiruvanmiyur', 'nandanam', 'nanganallur', 'kilpauk', 'gopalapuram', 'saligramam', 'manali', 'chromepet', 'rajajinagar', 'madhavaram', 'royapettah', 'virugambakkam', 'pallavaram', 'mannivakkam', 'surai', 'maduravoyal', 'malayambakkam', 'tiruvottiyur', 'ramapuram', 'perumanallur', 'puliyur', 'sholinganallur', 'madipakkam', 'chetput', 'selaiyur', 'mylapore', 'lakshmipuram', 'vandalur', 'kelambakkam', 'kothamangalam', 'tindivanam', 'nallur', 'ponnur', 'ambattur', 'kattur', 'irungattukottai', 'sathyamangalam', 'mangalam', 'tiruttani', 'thandalam', 'pondur', 'saram', 'varanavasi', 'nandambakkam', 'arakkonam', 'kuppam', 'villivakkam', 'kallur', 'sengadu', 'jangalapalli', 'sriperumbudur', 'kotturpuram', 'nolambur', 'kovur', 'mannur', 'gingee', 'kavanur', 'adyar', 'gummidipundi', 'karasangal', 'saidapet', 'panruti', 'narasingapuram', 'palamathi', 'gudalur', 'srinivasapuram', 'tiruvallikeni', 'reddipalayam', 'vadapalani', 'kasba', 'vadakkupattu', 'devadanam', 'gunduperumbedu', 'ayyampettai', 'vallur', 'kaveripak', 'arni', 'arcot', 'ponneri', 'alathur', 'bommayarpalayam', 'auroville', 'rajanagaram', 'salur', 'polivakkam', 'morai', 'karai', 'oragadam', 'vadakarai', 'ponnankuppam', 'venkatesapuram', 'arungunam', 'mukundarayapuram', 'pallikaranai', 'sattur', 'rangapuram', 'karunguli', 'konur', 'latteri', 'sunguvarchatram', 'ganapathipuram', 'manimangalam']
hyd_cluster_city_list = ['hyderabad', 'jaipur', 'secunderabad', 'warangal', 'karimnagar', 'kannur', 'hayatnagar', 'kukatpally', 'dindigul', 'rampur', 'khammam', 'bahadurpura', 'gandhinagar', 'rajendranagar', 'chandanagar', 'miyapur', 'alampur', 'gopalpur', 'nalgonda', 'adilabad', 'suraram', 'nizamabad', 'srinagar', 'alwal', 'almasguda', 'medak', 'ghatkesar', 'ramakrishnapuram', 'ramnagar', 'saidabad', 'gachibowli', 'mirzapur', 'kothaguda', 'sultanpur', 'bowenpally', 'krishnapur', 'trimulgherry', 'ida jeedimetla', 'mancherial', 'bhimavaram', 'nagaram', 'mallampet', 'nizampet', 'kokapet', 'raghunathpur', 'narayanpur', 'bahadurpalli', 'anandbagh', 'manikonda', 'balanagar township', 'proddatur', 'rajapur', 'porur', 'nehrunagar', 'siddipet', 'ibrahimpatnam', 'karur', 'uppal', 'kandi', 'sainikpuri', 'ashoknagar', 'khanapur', 'manovikasnagar', 'krishnapuram', 'balanagar', 'jalalpur', 'bhongir', 'gazipur', 'ramachandrapuram', 'tellapur', 'bodhan', 'kalyani', 'bela', 'faizabad', 'venkatapuram', 'mandamarri', 'bhimpur', 'mamidipalli', 'allipur', 'ratnagiri', 'pipri', 'hmt township', 'dharmapuri', 'ambala', 'vidyanagar', 'gandipet', 'gangapur', 'kandukur', 'koratla', 'bareguda', 'nandanam', 'neredmet', 'hasnabad', 'kamareddy', 'osmannagar', 'kothuru', 'vikasnagar', 'islampur', 'sangareddy', 'sirsilla', 'gopalapuram', 'shadnagar', 'peruru', 'yenugonda', 'rangapur', 'ghanpur', 'kistaram', 'allipuram', 'medchal', 'madhavaram', 'kothapet', 'chityala', 'tandur', 'manuguru', 'siddapur', 'pocharam', 'hanumanpura', 'ramapuram', 'pudur', 'gollapudi', 'malkapur', 'hajipur', 'sirsa', 'bellampalli', 'sonari', 'vikarabad', 'timmapur', 'hindupur', 'kondapur', 'chillapur', 'nirmal', 'rompalli', 'airforce academy', 'mallapur', 'pardi', 'wanaparthy', 'pipalpahad', 'jawahar nagar', 'padra', 'gollapalli', 'lakshmipuram', 'chennur', 'banswada', 'kothapeta', 'shamshabad', 'nallur', 'chevella', 'chincholi', 'balapur', 'ibrahimpur', 'repalle', 'nagapur', 'madapur', 'atmakur', 'gannavaram', 'ganapavaram', 'saidpur', 'kondur', 'gudivada', 'nizampur', 'jammikunta', 'mandra', 'allapur', 'chennaram', 'kasimpur', 'narsapur', 'venkatapur', 'hazipur', 'duddeda', 'domalpalli', 'patimatla', 'achutapuram', 'gannaram', 'gundala', 'gummadavalli', 'baswapur', 'kallur', 'krishnasagar', 'nakrekal', 'shankarpalli', 'bodu', 'tummalapalli', 'gopalapur', 'penugonda', 'gadapur', 'kollur', 'macharam', 'gudur', 'kandikatkur', 'chandi', 'kodur', 'manthani', 'lingapuram', 'govindapur', 'dharmavaram', 'madhapur', 'jinnaram', 'govindapuram', 'lingapur', 'dharmapuram', 'konapur', 'patancheru', 'alur', 'chandapur', 'gopalraopet', 'gundlapally', 'peddapuram', 'guduru', 'mukundapuram', 'gadwal', 'saidapur', 'shahabad', 'chengal', 'dondapadu', 'budharam', 'devapur', 'nandigaon', 'kotra', 'dilawarpur', 'pochampalli', 'husnabad', 'malakpet', 'lingala', 'chintakani', 'abbapur', 'basar', 'chintakunta', 'dharmaram', 'narayanapuram', 'gangaram', 'nandipet', 'tangallapalli', 'rajura', 'madhira', 'nagarkurnool', 'kasba', 'mubarakpur', 'mallareddipalli', 'nellipaka', 'vavilala', 'sultanabad', 'duddepudi', 'venkatagiri', 'vallur', 'mangalpalli', 'chittanur', 'narayanpet', 'ramagundam', 'gudem', 'mucherla', 'kotgir', 'pullur', 'raikal', 'rachapalli', 'elgandal', 'mardi', 'shamirpet', 'lakkavaram', 'polepalli', 'mulug', 'gokaram', 'sirpur', 'kummera', 'kacharam', 'asifabad', 'ramannapalem', 'yellapur', 'mahabubabad', 'suryapet', 'sangvi', 'sangam', 'gunj', 'inole', 'dharur', 'wyra', 'bhupalapatnam', 'rajaram', 'paidipalli', 'hasnapur', 'sripur', 'batasingaram', 'mallaram', 'mustipalli', 'mokila', 'akaram', 'chagal', 'pakhal', 'nallagonda', 'ramapur', 'kosgi', 'machapur', 'madharam', 'moinabad', 'kistapur', 'nagulapalli', 'ramayampet', 'sarapaka', 'rangapuram', 'damannapet', 'veldi', 'karanji', 'kothur', 'shapur', 'mailaram', 'degaon', 'magdi', 'kistampet', 'singaram', 'cherlapalli', 'machavaram', 'vinjamur', 'acharlagudem', 'girmapur', 'mustial', 'timmapuram', 'macherla']
pune_cluster_city_list = ['pune', 'solapur', 'kamalpur', 'rampur', 'yamunanagar', 'gopalpur', 'ahmednagar', 'akola', 'satara', 'sultanpur', 'viman nagar', 'akurdi', 'kalewadi', 'golegaon', 'rajapur', 'madh', 'nehrunagar', 'mundhva', 'indrayaninagar', 'bavdhan', 'kiwale', 'karad', 'khanapur', 'loni', 'shrirampur', 'jalalpur', 'vadgaon', 'dhanori', 'barshi', 'gondegaon', 'pathardi', 'borgaon', 'dharmapuri', 'vidyanagar', 'shirgaon', 'gangapur', 'jalgaon', 'islampur', 'khadki', 'dongargaon', 'mahim', 'daund', 'rajpur', 'pirangut', 'dehu', 'chakan', 'nanded', 'sadar bazar', 'pandharpur', 'phursungi', 'tarapur', 'malewadi', 'bhosarigoan', 'malkapur', 'baramati', 'sonari', 'nandgaon', 'wadgaon', 'sakegaon', 'khardi', 'kurduvadi', 'koregaon', 'bori', 'bhingar', 'narayangaon', 'khed', 'kedgaon', 'mandvi', 'deogaon', 'chincholi', 'malegaon', 'kopargaon', 'shindewadi', 'phaltan', 'karjat', 'shirur', 'arali', 'dhamangaon', 'chas', 'pasarni', 'mangrul', 'sangamner', 'patan', 'dahivad', 'pimpri', 'pimple gurav', 'patole', 'khadaki', 'sangola', 'bari', 'aundh', 'dapodi', 'sanaswadi', 'indapur', 'vilad', 'shikrapur', 'chandkhed', 'manjari', 'chanda', 'parner', 'asangaon', 'savali', 'uruli devachi', 'chinchani', 'bhokar', 'kauthali', 'baner', 'rahuri', 'saidapur', 'dhumalwadi', 'devapur', 'shirdi', 'koregaon mul', 'panoli', 'andhari', 'ghot', 'math', 'kudal', 'palashi', 'kasba', 'waki', 'khandgaon', 'ranjangaon', 'pargaon', 'bramhapuri', 'naigaon', 'chande', 'soregaon', 'jamkhed', 'koyali', 'mardi', 'darewadi', 'manchar', 'dahigaon', 'diksal', 'masur', 'sangvi', 'kotul', 'kasarwadi', 'chikhalgaon', 'tembhurni', 'rajewadi', 'khandala', 'hasnapur', 'shevgaon', 'karamba', 'mohol', 'medha', 'lonand', 'madha', 'bharatgaon', 'dahivadi', 'pimpalgaon', 'junnar', 'udapur', 'ghodegaon', 'mamdapur', 'dhamani', 'jamgaon', 'karanji', 'manegaon', 'murud', 'shapur', 'degaon', 'nagzari', 'songaon', 'jejuri', 'kolegaon', 'wangi', 'kolgaon', 'kumbhari', 'lonavala']
mumbai_cluster_city_list = ['mumbai', 'nashik', 'thane', 'kalyan city', 'raipur', 'bhiwandi', 'palghar', 'andheri east', 'panvel', 'ajmer', 'andheri', 'kopar', 'bandra west', 'ramnagar', 'dharavi', 'vashi', 'kharghar', 'kopar khairne', 'ojhar', 'beed', 'danda', 'rajapur', 'bangaon', 'pali', 'ulwa', 'sopara', 'juhu', 'vadgaon', 'padghe', 'kherwadi', 'gondegaon', 'puri', 'umela', 'pathardi', 'borgaon', 'satiwali', 'wadala', 'shirgaon', 'dari', 'gangapur', 'kurla', 'jalgaon', 'khadki', 'shahapur', 'parel', 'dongargaon', 'mahim', 'kurla west', 'tarapur', 'tehare', 'shivaji nagar', 'saykheda', 'jambhul', 'mahad', 'nandgaon', 'wadgaon', 'nirmal', 'khardi', 'gandhi nagar', 'alibag', 'sion', 'khed', 'worli', 'virar', 'mandvi', 'deogaon', 'chincholi', 'malegaon', 'nagaon', 'dwarka', 'karjat', 'dhamangaon', 'nagapur', 'chas', 'dapoli', 'mangrul', 'nagari', 'dahanu', 'dahivad', 'pimpri', 'patole', 'khandale', 'nizampur', 'jaskhar', 'n.s.karanja', 'manda', 'khadgaon', 'shirol', 'ganeshwadi', 'amba', 'mala', 'pen', 'asangaon', 'roha', 'vanjarwadi', 'chinchani', 'yeola', 'agashi', 'kaman', 'neral', 'khambale', 'saravali', 'waki', 'mangarul', 'pargaon', 'naigaon', 'chande', 'bordi', 'bhatgaon', 'dahigaon', 'girgaon', 'karanja', 'mohadi', 'rajewadi', 'dapur', 'medha', 'pimpalgaon', 'somta', 'bokadvira', 'rayate', 'mamdapur', 'vikhroli', 'jamgaon', 'manegaon', 'belgaon', 'degaon', 'mahiravani', 'uran', 'colaba']
banglore_cluster_city_list = ['kannur', 'gandhinagar', 'whitefield', 'rampura', 'ashoknagar', 'kolar', 'kattigenahalli', 'bellandur', 'bengaluru', 'hosur', 'channasandra', 'halehalli', 'rajajinagar', 'vijayapura', 'herohalli', 'robertsonpet', 'yelahanka', 'bommanahalli', 'goripalya', 'anekal', 'melur', 'sompura', 'kandavara', 'kempanahalli', 'kallur', 'chandragiri', 'channarayapatna', 'malur', 'mallasandra', 'kengeri', 'dodballapura', 'chikkanahalli', 'sira', 'chamrajpet', 'dharmaram', 'lakshmisagara', 'vallur', 'kalkere', 'hosahalli', 'dommasandra', 'devanahalli', 'chintamani', 'begur', 'kodihalli', 'sulur', 'mulbagal', 'thimmalapura', 'pura', 'kodigehalli', 'achalu', 'domlur', 'hoskote', 'channenahalli', 'kothur', 'tiptur', 'shapur', 'somanhalli', 'harohalli', 'attibele', 'bidadi', 'kanakapura']
delhi_cluster_city_list = ['delhi', 'gurgaon', 'ghaziabad', 'new delhi', 'gopalpur', 'anand', 'sahibabad', 'mirzapur', 'sultanpur', 'chandpur', 'model town', 'connaught place', 'kurali', 'sagarpur', 'vasant kunj', 'rajapur', 'khora', 'pali', 'khanpur', 'greater noida', 'dasna', 'jamalpur', 'chhapraula', 'vasundhra', 'faridpur', 'chhajarsi', 'ballabgarh', 'sakipur', 'loni', 'narsinghpur', 'jalalpur', 'sherpur', 'mahipalpur', 'wazirabad', 'alipur', 'fatehpur', 'bilaspur', 'faridabad', 'patiala', 'nagla charandas', 'housing board', 'dhankot', 'rajpur', 'shahpur', 'nawada', 'tilpat', 'jalalabad', 'sonipat', 'shivaji nagar', 'moti bagh', 'salempur', 'basantpur', 'hasanpur', 'anwali', 'dundahera', 'munirka', 'noida', 'bali', 'mathura', 'kasna', 'kaushambi', 'shahjahanpur', 'bhojpur', 'badarpur', 'saidpur', 'dadri', 'halalpur', 'nizampur', 'bari', 'dhaula kuan', 'khera', 'salarpur', 'kheri', 'sadarpur', 'niwari', 'basai', 'kulesra', 'vaishali', 'sikri khurd', 'kheri kalan', 'sersa', 'damdama', 'samaipur', 'jarcha', 'bhatgaon', 'kundal', 'sikandra', 'gohana', 'dayalpur', 'atrauli', 'chhapra', 'jainpur', 'mohana', 'bhaipur', 'baroli', 'kasan', 'akbarpur', 'garhi', 'nagla', 'shapur', 'bayanpur']
calcuta_cluster_city_list = ['chakpanchuria', 'krishnanagar', 'anantapur', 'jodhpur', 'murshidabad', 'barrackpore', 'manipur', 'raipur', 'aurangabad', 'kamalpur', 'rampur', 'alampur', 'gopalpur', 'maheshpur', 'srinagar', 'daudpur', 'dhapa', 'bediadanga', 'kota', 'basudebpur', 'gopinathpur', 'sitarampur', 'daulatpur', 'barasat', 'jagannathpur', 'ramnagar', 'saidabad', 'sultanpur', 'chandpur', 'krishnapur', 'santipur', 'habibpur', 'srirampur', 'durgapur', 'gobindapur', 'bahadurpur', 'habra', 'lalpur', 'bishnupur', 'fatepur', 'sadhanpur', 'khargram', 'raghunathpur', 'narayanpur', 'madanpur', 'balarampur', 'hatgacha', 'jagadishpur', 'mathurapur', 'rajapur', 'aswini nagar', 'bharatpur', 'mohanpur', 'dharampur', 'khanpur', 'azimganj', 'ganganagar', 'parulia', 'mamudpur', 'lalgola', 'east udayrajpur', 'ratanpur', 'kandi', 'bajitpur', 'raninagar', 'beldanga', 'bhagabanpur', 'faridpur', 'bangur avenue', 'baruipur', 'aligarh', 'nabagram', 'alipore', 'jalalpur', 'radhanagar', 'sherpur', 'gazipur', 'kalinagar', 'rasulpur', 'haripur', 'basirhat', 'chandipur', 'ramchandrapur', 'nagar', 'gopalnagar', 'choa', 'kalyani', 'bhabanipur', 'deuli', 'bara', 'bhimpur', 'shibpur', 'bahara', 'ichhapur', 'kalikapur', 'balia', 'nimtita', 'bilaspur', 'shyampur', 'sitapur', 'madhabpur', 'nabadwip', 'kapasdanga', 'bolpur', 'rajarhat gopalpur', 'sahapur', 'kalipur', 'madhupur', 'kulti', 'bhandara', 'hasnabad', 'dangapara', 'bhadrapur', 'chittaranjan', 'jadupur', 'tajpur', 'dewas', 'nabasan', 'bhagirathpur', 'gobardanga', 'joynagar', 'kodalia', 'hridaypur', 'baranagar', 'ganeshpur', 'ghoshpur', 'birpur', 'vip nagar', 'kaliganj', 'santoshpur', 'ghola', 'suri', 'kalindi housing estate', 'nutangram', 'salua', 'mukundapur', 'radhakantapur', 'tentulia', 'tarapur', 'gokulpur', 'paikpara', 'chapra', 'bataspur', 'joykrishnapur', 'tollygunge', 'lakshmipur', 'khidirpur', 'shyamnagar', 'rampara', 'simulia', 'new town', 'manoharpur', 'baruipara', 'daspur', 'garaimari', 'brindabanpur', 'belpukur', 'basantpur', 'ranaghat', 'hasanpur', 'ghuni', 'gadadharpur', 'sankarpur', 'kanchanpur', 'bhawanipur', 'dogachhia', 'belgharia', 'chandipur tarapith', 'bikrampur', 'maharajganj', 'barakartickchungri', 'kumarpur', 'bali', 'ambikapur', 'laskarpur', 'baikunthapur', 'kholapota', 'kashipur', 'chandanpur', 'bandipur', 'ambhua', 'mahalandi', 'hariharpara', 'sarangpur', 'barwan', 'dwarka', 'bhairabpur', 'gokarna', 'hatia', 'rampurhat', 'diamond harbour', 'ahiran', 'nalhati', 'srikrishnapur', 'aushberia', 'nagari', 'khilkapur', 'milki', 'kedarpur', 'baduria', 'dubrajpur', 'gaighata', 'nischintapur', 'hatisala', 'bharatgarh', 'baidyanathpur', 'nayabad', 'mandra', 'baidyapur', 'kantur', 'nurpur', 'godda', 'jhikra', 'lalbazar', 'hatiara', 'beonta', 'bamunpukur', 'ramkrishnapur', 'jafarpur', 'debipur', 'kanakpur', 'basudevpur', 'bedibhavan', 'banglani', 'biswanathpur', 'bamanpukur', 'debagram', 'berhampore', 'brahmandihi', 'muradpur', 'digha', 'gopalganj', 'palsunda', 'bhanderkhola', 'salar', 'chandi', 'sujapur', 'paharpur', 'mithipur', 'murarai', 'kalidaspur', 'mahadebnagar', 'gonipur', 'alamsahi', 'sadarpur', 'sainthia', 'adampur', 'dadpur', 'kakdwip', 'karnamadhabpur', 'benipur', 'adityapur', 'binodpur', 'kamdebpur', 'dighra', 'arampur', 'belgram', 'rajnagar', 'jajigram', 'basar', 'baneswarpur', 'barbaria', 'shibrampur', 'ahmadpur', 'jasaikati', 'palashi', 'kasba', 'hazratpur', 'bhagabatipur', 'prafulla kanan', 'chanduria', 'jamira', 'rameswarpur', 'haridaspur', 'sonda', 'kogram', 'harinarayanpur', 'mahadevpur', 'mongalganj', 'rajarampur', 'gangasagar', 'harindanga', 'damdama', 'madanganj', 'sagar', 'begampur', 'barapahari', 'gadigachha', 'chatra', 'bishannagar', 'beharia', 'rudrapur', 'rautara', 'karimpur', 'atai', 'jafrabad', 'purandarpur', 'daria', 'amghata', 'chaitpur', 'narasinghapur', 'panpur', 'kundal', 'bahagalpur', 'patharghata', 'budhakhali', 'nabipur', 'mirpur', 'illambazar', 'ram nagar', 'panchpota', 'brahmanpara', 'sripur', 'mansadwip', 'khaspur', 'banagram', 'andulia', 'madarpur', 'michealnagar', 'khulna', 'aswathtala', 'sangrampur', 'barnia', 'bagchi', 'monoharpur', 'madhaipur', 'patuli', 'takipur', 'bharkata', 'basirhat college', 'chikanpara', 'kirtipur', 'burul', 'aturia', 'nazirpur', 'sarberia', 'kabilpur', 'keorakhali', 'kumari', 'dariapur', 'jaleswar', 'tungi', 'nayagram', 'chaital', 'barunhat', 'chautara', 'diha', 'hatgachha', 'gayeshpur', 'hingalganj', 'jogendranagar', 'kulgachi', 'mitrapur', 'lohapur', 'korachandigarh']

cluster_map = {'chennai': 'CLUSTER_CHE_V2', 'hyd': 'CLUSTER_HYD_V2', 'pune': 'CLUSTER_PUN_V2', 'mumbai': 'CLUSTER_MUM_V2', 'banglore': 'CLUSTER_BEN_V2',
               'delhi': 'CLUSTER_NCR_V2', 'calcuta': 'CLUSTER_KOL_V2'}
cluster_map_id = {'chennai': 55000008, 'hyd': 55000009, 'pune': 55000010, 'mumbai': 55000011, 'banglore': 55000012,
                  'delhi': 55000013, 'calcuta': 55000014}
cluster_state_map = {'chennai': 'tn', 'hyd': 'ts', 'pune': 'mh', 'mumbai': 'mh', 'banglore': 'ka',
                     'delhi': 'dl', 'calcuta': 'wb'}
cluster_expansion_map = {'Chennai': 'chennai', 'Hyderabad': 'hyd', 'Pune': 'pune', 'Mumbai': 'mumbai', 'Bangalore': 'banglore',
                                             'Delhi NCR': 'delhi', 'Kolkata': 'calcuta'}
cluster_expansion_map_v2 = {'Chennai V2': 'chennai', 'Hyderabad V2': 'hyd', 'Pune V2': 'pune', 'Mumbai V2': 'mumbai', 'Bangalore V2': 'banglore',
                         'Delhi NCR V2': 'delhi', 'Kolkata V2': 'calcuta'}
cluster_city_map = {'chennai': chennai_cluster_city_list, 'hyd': hyd_cluster_city_list, 'pune': pune_cluster_city_list,
                    'mumbai': mumbai_cluster_city_list, 'banglore': banglore_cluster_city_list, 'delhi': delhi_cluster_city_list,
                    'calcuta': calcuta_cluster_city_list}

location_insert_statement_sample = "INSERT INTO adtech_master_data.location_cluster_mapping (cluster_id, location_id) VALUES ((select id from adtech_master_data.location_cluster where targeting_tag = '{0}'), (select id from adtech_master_data.city where targeting_tag = '{1}'));"
columns = ['cluster_id', 'location_id']
temp_location_id = 51626799
df_location_cluster_mapping_insert = pd.DataFrame(columns=columns)

columns_dup = ['city', 'cluster_list', 'pulse_state_code']
df_dup_cluster_city = pd.DataFrame(columns=columns_dup)
# print(cluster_map)

check_unique= []
check_map = {}
check_city_cluster = {"a":[""]}
df_final_pulse_city = pd.read_csv("./input/test/first.csv")

df_old_lcm = pd.read_csv("input/location_cluster/lcm_city_dump.csv")
df_old_lcm['city'] = df_old_lcm['city'].str.lower()
old_lcm_city_list = df_old_lcm['city'].values.tolist()
common_city_count_check = 0
check_cluster_match_count = 0
common_city_list = {}
# pudur (og: chennai, new_mapping: hyd)
# madh (mumbai, pune)

# ---------------
# check the state code and cluster state are matching or not.
# ---------------
# check_same_state_count = 0
# c_s = 0
# df_dup_city_lc_state = pd.read_csv('./output/dup_city_lc_state.csv').fillna('empty')
# dup_city_lc_list = df_dup_city_lc_state['city'].values.tolist()
# df_lc_v2_after_dedup = pd.read_csv('./input/LocationClusterV2_after_remove_dedup.csv')
# city_lc_list = df_lc_v2_after_dedup['city_name'].values.tolist()
#
# for row in df_lc_v2_after_dedup.itertuples():
#     each_city = row.city_name.lower()
#     cluster = row.location_cluster_name
#     comment = row.Comment
#     df_each_city_state = df_final_pulse_city.loc[df_final_pulse_city['city']==each_city]
#     each_pulse_state_code_check = df_each_city_state.iloc[0, 2]
#     each_pulse_state_code_check = 'ts' if(each_pulse_state_code_check == 'ap') else each_pulse_state_code_check
#     # each_pulse_state_code_check = 'dl' if(each_pulse_state_code_check == 'up') else each_pulse_state_code_check
#
#     if(cluster_state_map[cluster_expansion_map_v2[cluster]] == each_pulse_state_code_check):
#         check_same_state_count += 1
#     elif(comment == 'Remove' and cluster_state_map[cluster_expansion_map_v2[cluster]] != each_pulse_state_code_check):
#         c_s += 1
#         pass
#     else:
#         # print(each_city, "=> ",cluster_state_map[cluster_expansion_map_v2[cluster]], '  <------------->  ', each_pulse_state_code_check)
#         file_state_check.write(each_city + ',' + row.location_cluster_name + ',' + row.state_name)
#         file_state_check.write('\n')

for cluster in cluster_map:
    cluster_addition = cluster + "_cluster_city_list"

    for each_city in cluster_city_map[cluster]:
        check_unique.append(each_city) #check the count of each tag
        if(each_city not in check_map):
            check_map[each_city] = 1
            check_city_cluster[each_city] = [cluster]
        else:
            check_map[each_city] += 1
            check_city_cluster[each_city].append(cluster)

        if(each_city in old_lcm_city_list): #check if the city is already present or not?
            common_city_count_check += 1
            df_city = df_old_lcm.loc[df_old_lcm['city']==each_city]
            common_city_list[each_city] = cluster_expansion_map[df_city.iloc[0, 2]]
            if(cluster == common_city_list[each_city]):
                check_cluster_match_count += 1
            else:
                pass
                # print(cluster, common_city_list[each_city], each_city)

        name = each_city.title()
        targeting_tag = 'CITY_' + name.upper().replace(" ", "_")
        insert_statement = location_insert_statement_sample.format(cluster_map[cluster], targeting_tag)
        file.write(insert_statement)
        file.write("\n")

        row = [cluster_map_id[cluster], temp_location_id]
        df_location_cluster_mapping_insert = pd.concat([df_location_cluster_mapping_insert, pd.DataFrame([row], columns=columns)], ignore_index=True)
        temp_location_id += 1

print(common_city_count_check)
print(check_cluster_match_count)
print(len(common_city_list))
print(len(set(common_city_list)))

# --------------
# old city difference check
# --------------
df_old_lcm = df_old_lcm.loc[~df_old_lcm['city'].isin(common_city_list.keys())]
# df_old_lcm.to_csv("./output/lc_cities_not_in_pulse.csv", index = False)
print(len(df_old_lcm))

# duplicate location cluster city checking in the new mappings
dup_map = {} #['city', 'cluster_list', 'pulse_state_code']
print(len(check_unique))
print(len(set(check_unique)))
check_dup_count = 0

for i in check_map:
    if(check_map[i]!=1):
        # print( check_city_cluster[i])
        df_each_city = df_final_pulse_city.loc[df_final_pulse_city['city']==i]
        each_pulse_state_code = df_each_city.iloc[0, 2]
        # row = {'city': i, 'cluster_list': check_city_cluster[i], 'pulse_state_code': each_pulse_state_code}
        row = [i, check_city_cluster[i], each_pulse_state_code]
        # print(row)
        # print(i, check_city_cluster[i])
        df_dup_cluster_city = pd.concat([df_dup_cluster_city, pd.DataFrame([row], columns=columns_dup)], ignore_index=True)
        # check_city_cluster[i].append(each_pulse_state_code)
        dup_map[i] = check_map[i]
        check_dup_count += check_map[i]

# print(check_same_state_count)
# print(c_s)

# print(len(check_map))
# print(len(dup_map))
# print(check_dup_count)
# df_dup_cluster_city.to_csv("output/dup_city_lc_state.csv")
# df_location_cluster_mapping_insert.to_csv("output/new_lc_mapping_insert_automation.csv", sep='|', index=False)


# --------------
# check if the existing location cluster is matching the new delhi cities or not.
# ---------------
new_delhi_lc_cities = ['Gandhi Nagar','Najafgarh','Kalkaji','delhi','new delhi','kalkaji','connaught place','babarpur','nasirpur','nimri','sagarpur','ranhola','vasant kunj','burari','deoli','rani bagh','shalamar','baprola','siraspur','najafgarh','rithala','shahpur jat','hauz khas','mundka','jagatpur','mahipalpur','barwala','patparganj','lajpat nagar','shahdara','jaitpur','darya ganj','zakhira','yamuna vihar','lado sarai','east of kailash','karala','jharoda kalan','mehrauli','chattarpur','dindarpur','bawana','maidan garhi','seelampur','samalkha','pahar ganj','narela','moti bagh','timarpur','chaukhandi','munirka','maujpur','jhilmil','kakrola','bakoli','badarpur','bankner','greater kailash','karol bagh','safdarjung enclave','tughlakabad','bhorgarh','dhaula kuan','ghoga','rani khera','gadaipur','gheora']
city_not_in_current_lcm = list()

df_edge_pulse_table = pd.read_csv("./input/city_table_pulse_migration.csv")
df_edge_pulse_table['name'] = df_edge_pulse_table['name'].str.lower()
edge_pulse_cities = df_edge_pulse_table['name'].values.tolist()
# c_exising_city_table = 0
state_code = set()

df_existing_lcm_table = pd.read_csv("./input/location_cluster/existing_lcm_prod_table.csv")
df_existing_lcm_table['name'] = df_existing_lcm_table['name'].str.lower()
df_existing_lcm_table = df_existing_lcm_table[df_existing_lcm_table['cluster_id']==55000014]
existing_lcm_cities = df_existing_lcm_table['name'].values.tolist()
c_exising_lcm_table = 0
# new_delhi_list = ['gandhi nagar','babarpur', 'nasirpur', 'nimri', 'ranhola', 'burari', 'deoli', 'rani bagh', 'shalamar', 'baprola', 'siraspur', 'rithala', 'shahpur jat', 'hauz khas', 'mundka', 'jagatpur', 'barwala', 'patparganj', 'lajpat nagar', 'jaitpur', 'zakhira', 'yamuna vihar', 'lado sarai', 'east of kailash', 'karala', 'dindarpur', 'maidan garhi', 'seelampur', 'pahar ganj', 'timarpur', 'chaukhandi', 'maujpur', 'jhilmil', 'kakrola', 'bakoli', 'bankner', 'greater kailash', 'safdarjung enclave', 'tughlakabad', 'bhorgarh', 'ghoga', 'rani khera', 'gadaipur', 'gheora']
new_delhi_list = []
for city in new_delhi_lc_cities:
    city = city.lower()
    df_each_city = df_edge_pulse_table.loc[df_edge_pulse_table['name']==city]
    state_code.add(df_each_city.iloc[0, 4])

    # if(df_each_city.iloc[0, 4] == 52010036):
    #     print(df_each_city)
    if(city in existing_lcm_cities or city in new_delhi_list):
        c_exising_lcm_table += 1
    else:
        city_not_in_current_lcm.append(city)

    # print(city)
print(city_not_in_current_lcm)
print(c_exising_lcm_table)
print(state_code)