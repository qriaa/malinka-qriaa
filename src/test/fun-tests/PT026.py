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
    products = list()
    promotions = list()
    for prod in addedProducts:
        cursor.execute(f"SELECT ID_PRODUKTU, CENA FROM PRODUCT WHERE ID_PRODUKTU = {prod}")
        products.append(cursor.fetchone())
    
    for promo in addedPromotions:
        cursor.execute(f"SELECT ID_PRODUKTU, ZNIZKA FROM PROMOCJA WHERE ID = {promo}")
        promotions.append(cursor.fetchone())
    i = 0
    while i < len(promotions):
        prod = promotions[i]
        promotions[i] = (prod[0], round(prod[1], 2))
        i+=1

    expectedSum = 0
    i = 0
    while i < len(products):
        prod = products[i]
        prodSum = prod[1]
        j = 0
        while j < len(promotions):
            promo = promotions[j]
            if prod[0] == promo[0]:
                prodSum *= promo[1]
            j+=1
        expectedSum += prodSum
        i+=1
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
        testMsg("Calculated total does not match the database!")
        return

    addProduct(driver, 0)

    if not checkIfSumIsCorrect(driver, productIds, promotionIds, conn):
        testMsg("Calculated total does not match the database!")
        return

    addPromotion(driver, 0)

    if not checkIfSumIsCorrect(driver, productIds, promotionIds, conn):
        testMsg("Calculated total does not match the databases!")
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
            testMsg("OfferProduct have not been saved!")
            return

    for promo in promotionIds:
        if promo not in actualSavedOfferPromotions:
            testMsg("OfferPromotions have not been saved!")
            return
    
    testMsg("Test passed")


def PT02603402():
    def testMsg(message):
        print("PT026-034-02: " + message)

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
        testMsg("Calculated total does not match the database!")
        return

    addProduct(driver, 0)

    if not checkIfSumIsCorrect(driver, productIds, promotionIds, conn):
        testMsg("Calculated total does not match the database!")
        return

    addPromotion(driver, 0)

    if not checkIfSumIsCorrect(driver, productIds, promotionIds, conn):
        testMsg("Calculated total does not match the databases!")
        return
    
    offerId = driver.find_element(By.XPATH, '//*[@id="offerId"]').get_attribute("value")

    cancelOffer = driver.find_element(By.XPATH, '/html/body/div[2]/a[2]')
    cancelOffer.click()

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


    if len(actualSavedId) != 0:
        testMsg("Offer has been saved!")
        return

    for prod in productIds:
        if prod in actualSavedOfferProducts:
            testMsg("OfferProduct have been saved!")
            return

    for promo in promotionIds:
        if promo in actualSavedOfferPromotions:
            testMsg("OfferPromotions have been saved!")
            return
    
    testMsg("Test passed")


def PT02603601():
    def testMsg(message):
        print("PT026-036-01: " + message)

    driver = webdriver.Firefox()

    driver.get("http://localhost:9000/manager/offer")

    EdytujOferte = driver.find_element(By.XPATH, '/html/body/div[2]/div/a[1]')
    EdytujOferte.click()

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

    ProduktyList = driver.find_element(By.XPATH, '//*[@id="productSection"]/div[2]')
    ProduktyList = ProduktyList.find_elements(By.TAG_NAME, "form")
    ProduktyList.pop(0)
    for form in ProduktyList:
        productIds.append(form.find_element(By.TAG_NAME, "input").get_attribute("value"))

    PromocjeList = driver.find_element(By.XPATH, '//*[@id="promotionSection"]/div[2]')
    PromocjeList = PromocjeList.find_elements(By.TAG_NAME, "form")
    PromocjeList.pop(0)
    for form in PromocjeList:
        promotionIds.append(form.find_element(By.TAG_NAME, "input").get_attribute("value"))



    if not checkIfSumIsCorrect(driver, productIds, promotionIds, conn):
        testMsg("Calculated total does not match the database!")
        driver.close()
        return

    addProduct(driver, 0)

    if not checkIfSumIsCorrect(driver, productIds, promotionIds, conn):
        testMsg("Calculated total does not match the database!")
        driver.close()
        return

    addPromotion(driver, 0)

    if not checkIfSumIsCorrect(driver, productIds, promotionIds, conn):
        testMsg("Calculated total does not match the databases!")
        driver.close()
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
            testMsg("OfferProduct have not been saved!")
            return

    for promo in promotionIds:
        if promo not in actualSavedOfferPromotions:
            testMsg("OfferPromotions have not been saved!")
            return
    
    testMsg("Test passed")


def PT02603602():
    def testMsg(message):
        print("PT026-036-02: " + message)

    driver = webdriver.Firefox()

    driver.get("http://localhost:9000/manager/offer")

    EdytujOferte = driver.find_element(By.XPATH, '/html/body/div[2]/div/a[1]')
    EdytujOferte.click()

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

    ProduktyList = driver.find_element(By.XPATH, '//*[@id="productSection"]/div[2]')
    ProduktyList = ProduktyList.find_elements(By.TAG_NAME, "form")
    ProduktyList.pop(0)
    for form in ProduktyList:
        productIds.append(form.find_element(By.TAG_NAME, "input").get_attribute("value"))

    PromocjeList = driver.find_element(By.XPATH, '//*[@id="promotionSection"]/div[2]')
    PromocjeList = PromocjeList.find_elements(By.TAG_NAME, "form")
    PromocjeList.pop(0)
    for form in PromocjeList:
        promotionIds.append(form.find_element(By.TAG_NAME, "input").get_attribute("value"))

    initialProductIds = productIds.copy()
    initialPromotionIds = promotionIds.copy()

    if not checkIfSumIsCorrect(driver, productIds, promotionIds, conn):
        testMsg("Calculated total does not match the database!")
        driver.close()
        return

    addProduct(driver, 0)

    if not checkIfSumIsCorrect(driver, productIds, promotionIds, conn):
        testMsg("Calculated total does not match the database!")
        driver.close()
        return

    addPromotion(driver, 0)

    if not checkIfSumIsCorrect(driver, productIds, promotionIds, conn):
        testMsg("Calculated total does not match the databases!")
        driver.close()
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
        testMsg("Offer has been deleted!")
        return

    for prod in initialProductIds:
        if prod not in actualSavedOfferProducts:
            testMsg("OfferProducts have been modified!")
            return

    for promo in initialPromotionIds:
        if promo not in actualSavedOfferPromotions:
            testMsg("OfferPromotions have been modified!")
            return
    
    prodDiff = []
    for element in productIds:
        if element not in initialProductIds:
            prodDiff.append(element)

    for prod in prodDiff:
        if prod in actualSavedOfferProducts:
            testMsg("OfferProducts have been modified!")
            return
    
    promoDiff = []
    for element in promotionIds:
        if element not in initialPromotionIds:
            promoDiff.append(element)

    for promo in promoDiff:
        if promo in actualSavedOfferPromotions:
            testMsg("OfferPromotions have been modified!")
            return
        
    testMsg("Test passed")


def PT02603501():
    def testMsg(message):
        print("PT026-035-01: " + message)

    driver = webdriver.Firefox()

    driver.get("http://localhost:9000/manager/offer")

    deleteButton = driver.find_element(By.XPATH, '/html/body/div[2]/div[2]/a[2]')
    offerId = deleteButton.get_attribute("href").split("=")[1]
    deleteButton.click()

    driver.close()

    cursor = conn.cursor()
    cursor.execute(f"SELECT ID FROM OFERTA WHERE ID={offerId}")
    actualDeletedId = cursor.fetchall()

    cursor.execute(f"SELECT ID_PRODUKTU FROM OFERTA_PRODUKT WHERE ID_OFERTY={offerId}")
    actualDeletedOfferProducts = cursor.fetchall()

    cursor.execute(f"SELECT ID_PROMOCJI FROM OFERTA_PROMOCJA WHERE ID_OFERTY={offerId}")
    actualDeletedOfferPromotions = cursor.fetchall()

    if len(actualDeletedId) != 0:
        testMsg("Offer has not been deleted!")
        return

    if len(actualDeletedOfferProducts) != 0:
        testMsg("Offers\' OfferProducts have not been deleted!")
        return

    if len(actualDeletedOfferPromotions) != 0:
        testMsg("Offers\' OfferPromotions have not been deleted!")
        return
        
    testMsg("Test passed")


PT02603401()
PT02603402()
PT02603601()
PT02603602()
PT02603501()