import jaydebeapi
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import Select

# Connect to the database
conn = jaydebeapi.connect("org.h2.Driver", "jdbc:h2:tcp://localhost:9092/~/PO-db", ["sa", "password"], "./fun-tests/h2-2.1.214.jar")

def checkIfSumIsCorrect(driver, addedProducts, addedPromotions, conn):
    sumElement = driver.find_element(By.XPATH, '/html/body/div[2]/div[2]')
    sumStr = sumElement.text
    actualSumValue = float(sumStr.split(' ')[1])

    cursor = conn.cursor()
    cursor.execute(f"SELECT ID_PRODUKTU, CENA FROM PRODUCT WHERE ID_PRODUKTU IN {'(' + ','.join(addedProducts) + ')'}")
    products = cursor.fetchall()
    cursor.execute(f"SELECT ID_PRODUKTU, ZNIZKA FROM PROMOCJA WHERE ID_PRODUKTU IN {'(' + ','.join(addedPromotions) + ')'}")
    promotions = cursor.fetchall()
    for i, prod in enumerate(promotions):
        promotions[i] = (prod[0], round(prod[1], 2))
    expectedSum = 0
    for prod in products:
        prodSum = prod[1]
        for promo in promotions:
            if prod[0] == promo[0]:
                prodSum *= promo[1]
        expectedSum += prodSum
    expectedSum = round(expectedSum, 2)
    return expectedSum == actualSumValue




def PT02603401():
    def testMsg(message):
        print("PT026-034-01: " + message)

    driver = webdriver.Firefox()

    driver.get("http://localhost:9000/manager/offer")

    DodajOferte = driver.find_element(By.XPATH, "/html/body/a[2]")
    DodajOferte.click()

    productIds = list()
    promotionIds = list()

    def addProduct(driver, index):
        ProduktySelect = driver.find_element(By.XPATH, '//*[@id="productId"]')
        ProduktyOptions = ProduktySelect.find_elements(By.TAG_NAME, "option")
        ProduktyAdd = driver.find_element(By.XPATH, '//*[@id="productSection"]/div[2]/form[1]/button')
        ProduktySelect.click()
        ProduktyOptions[index].click()
        productIds.append(ProduktyOptions[index].get_attribute("value"))
        ProduktyAdd.click()

    def addPromotion(driver, index):
        PromocjeSelect = driver.find_element(By.XPATH, '//*[@id="promotionId"]')
        PromocjeOptions = PromocjeSelect.find_elements(By.TAG_NAME, 'option')
        PromocjeAdd = driver.find_element(By.XPATH, '//*[@id="promotionSection"]/div[2]/form/button')
        PromocjeSelect.click()
        PromocjeOptions[index].click()
        promotionIds.append(PromocjeOptions[index].get_attribute("value"))
        PromocjeAdd.click()

    if not checkIfSumIsCorrect(driver, productIds, promotionIds, conn):
        testMsg("Calculated total does not match the databases\'!")
        return

    addProduct(driver, 0)

    if not checkIfSumIsCorrect(driver, productIds, promotionIds, conn):
        testMsg("Calculated total does not match the databases\'!")
        return

    addPromotion(driver, 0)

    if not checkIfSumIsCorrect(driver, productIds, promotionIds, conn):
        testMsg("Calculated total does not match the databases\'!")
        return
    
    offerId = driver.find_element(By.XPATH, '//*[@id="offerId"]').get_attribute("value")

    saveOffer = driver.find_element(By.XPATH, '/html/body/div[2]/a[1]')
    saveOffer.click()

    driver.close()

    cursor = conn.cursor()
    cursor.execute(f"SELECT ID FROM OFERTA WHERE ID={offerId}")
    actualSavedId = cursor.fetchall()

    cursor.execute(f"SELECT ID_PRODUKTU FROM OFERTA_PRODUKT WHERE ID_OFERTY={offerId}")
    actualSavedOfferProducts = cursor.fetchall()
    tempList = list()
    for row in actualSavedOfferProducts:
        tempList.append(str(row[0]))
    actualSavedOfferProducts = tempList

    cursor.execute(f"SELECT ID_PROMOCJI FROM OFERTA_PROMOCJA WHERE ID_OFERTY={offerId}")
    actualSavedOfferPromotions = cursor.fetchall()
    tempList = list()
    for row in actualSavedOfferPromotions:
        tempList.append(str(row[0]))
    actualSavedOfferPromotions = tempList


    if len(actualSavedId) == 0:
        testMsg("Offer has not been saved!")
        return

    for prod in productIds:
        if prod not in actualSavedOfferProducts:
            testMsg("OfferProduct has not been saved!")
            return

    for promo in promotionIds:
        if promo not in actualSavedOfferPromotions:
            testMsg("OfferPromotions has not been saved!")
            return
    
    testMsg("Test passed")

PT02603401()