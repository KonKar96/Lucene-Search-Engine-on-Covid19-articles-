from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver import ActionChains
from selenium.webdriver.chrome.options import Options
from selenium.common import exceptions
from selenium.common.exceptions import TimeoutException
from selenium.common.exceptions import NoSuchElementException


PATH = "chromedriver.exe"
options = webdriver.ChromeOptions()
options.add_argument('--window-size=1920,1080')
options.add_argument('--ignore-certificate-errors')
options.add_extension('UBlock.crx')
prefs = {'profile.default_content_setting_values': {'images': 2, 
                            'plugins': 2, 'popups': 2, 'geolocation': 2, 
                            'notifications': 2, 'auto_select_certificate': 2, 'fullscreen': 2, 
                            'mouselock': 2, 'mixed_script': 2, 'media_stream': 2, 
                            'media_stream_mic': 2, 'media_stream_camera': 2, 'protocol_handlers': 2, 
                            'ppapi_broker': 2, 'automatic_downloads': 2, 'midi_sysex': 2, 
                            'push_messaging': 2, 'ssl_cert_decisions': 2, 'metro_switch_to_desktop': 2, 
                            'protected_media_identifier': 2, 'app_banner': 2, 'site_engagement': 2, 
                            'durable_storage': 2}}
options.add_experimental_option('prefs', prefs)
driver = webdriver.Chrome(PATH, options=options)
d = {}


driver.get('https://www.thelancet.com/coronavirus/collection?pageSize=100&startPage=&ContentItemCategory=Editorial')
hrefs = []

for q in range(0,2):
    articles = driver.find_elements_by_xpath("//h4/a")
    for i in articles:
        i.location_once_scrolled_into_view
        hrefs.append(i.get_attribute('href'))
    driver.get('https://www.thelancet.com/coronavirus/collection?startPage=1&ContentItemCategory=Editorial&pageSize=100')

for i in hrefs:
    driver.get(i)
    text = ''
    try:
        title = WebDriverWait(driver, 5).until(EC.presence_of_element_located((By.XPATH,"//h1[@class='article-header__title']"))).text
        elements = driver.find_elements_by_xpath("//div[@class='section-paragraph' and not(descendant::div) and not(parent::section)]")
        for j in range(0, len(elements)-1):
            text+=elements[j].text
        d[title] = text
        print(title)
        print(text)
    except(NoSuchElementException,TimeoutException):
        continue

driver.get('https://www.the-scientist.com/tag/covid-19')
hrefs = []

while(True):
    articles = driver.find_elements_by_xpath("//a[@target='_self']")
    if articles[0].get_attribute('href') in hrefs:
        break
    for i in articles:
        i.location_once_scrolled_into_view
        hrefs.append(i.get_attribute('href'))
    driver.find_element_by_xpath("//button[@title='Next']").click()

for i in hrefs:
    driver.get(i)
    text = ''
    try:
        title = WebDriverWait(driver, 5).until(EC.presence_of_element_located((By.XPATH,"//header/h1"))).text
        elements = driver.find_elements_by_xpath("//div[@class='non-paywall']/p[not(@class='fr-hero-header-caption fr-caption fr-right fr-quarter')]")
        for j in range(0, len(elements)-1):
            text+=elements[j].text
        d[title] = text
        print(title)
        print(text)
    except(NoSuchElementException,TimeoutException):
        continue

i = 1
for key in d.keys():
    with open( (str(i) + '.csv'), 'w', encoding='utf8') as f:
        f.write("%s/-/%s\n"%(key,d[key]))
    i+=1

driver.close()
