# The Cognitive System Command Line Interface - CSCLI

This is a command line interface (CLI) to easily create [CST projects](https://cst.fee.unicamp.br).

## Installation

### Install from source

1. Clone this repository into a local folder
    ```shell
   git clone https://github.com/brgsil/CST-CLI.git
   ```
2. Go into the repository directory and run the package task
```shell
cd CST-CLI
./gradlew jpackage
```
3. Install deb package
```shell
sudo apt install ./build/cscli-package/cscli_0.1-1_amd64.deb
```
4. Update `PATH` variable to include executable
```shell
echo 'export PATH="$PATH:/opt/cscli/bin"' >> ~/.bashrc
source ~/.bashrc
```

## Example

To create a new example CST project run the command:
```shell
cscli init -f test.yaml
```