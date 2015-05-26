1. Introduction
GingaToolbox is a software package which allows a systematic processing of the
Ginga X-ray satellite data.

GingaToolbox has been developed as part of a Master Thesis project for the
Valencian International University: 
Spectral State Transitions in Low-Mass X-ray Binaries observed with Ginga.

GingaToolbox is distributed as an open-source software package with GPL License.

2. GPL License

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

3. GingaToolbox Virtual Machine (GingaToolboxVM)

A version of the GingaToolbox with all the required data and software installed
has been already packaged 
into a Virtual Machine. The Virtual Machine includes a copy of the ISAS/JAXA
Ginga Data Archive, the ISAS/JAXA Ginga Tools, and a PostgreSQL database server
where all the Ginga orbit, housekeeping and attitude files have been stored
together with the Ginga observations catalogue. The Virtual machine can be
downloaded form here: https://copy.com/z0oapWLK1f3sMaKX

The version of the GingaToolbox software running into the GingaToolboxVM can be
updated. To do so you can open a terminal and execute the following steps:

i. Go to the source code local repository folder
> cd ~/ginga-toolbox.git

ii. Fetch latest source code from server
> git pull
[password: gingatoolbox]

iii. Build the software
> mvn clean package assembly:assembly
iv. Install the software
> cp target/ginga-toolbox-<X.Y>.tar.gz ~/
> cd ~/
> rm ginga-toolbox
> ln -s ginga-toolbox-<X.Y> ginga-toolbox


4. Local Installation

If want to make a local installation instead, you can read the steps followed to
create the GingaToolboxVM on docs/HOWTO_GingaToolboxVM.txt. 
The sofware pre-requisites are the following:

* Java 1.7 or higher
* MySQL or PostgreSQL database 
* Apache Maven
* Git client
* ISAS/JAXA Ginga Tools, which has its own software requirements
(http://www.darts.isas.jaxa.jp/astro/ginga/analysis.html)