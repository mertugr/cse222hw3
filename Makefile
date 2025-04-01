collect:
	dir /s /b *.java > sources.txt

build:
	javac -d build @sources.txt

run:
	java -cp build main.Main

clean:
		@if exist $(BUILD_DIR) rmdir /s /q $(BUILD_DIR) 